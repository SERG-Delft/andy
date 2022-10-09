import os, re, sys
from git import Repo
from shutil import copyfile
from xml.dom import minidom

def get_directories(basedir):
    return [os.path.join(basedir, dir) for dir in os.listdir(basedir) \
                                       if os.path.isdir(os.path.join(basedir, dir)) \
                                       and not dir.startswith('.')]

# Compile Andy and store the `target` directory.
home_dir = '/home/runner/work/andy/andy/assignments'

# Clone the assignments repository in a temporary directory.
Repo.clone_from('https://github.com/cse1110/assignments', home_dir, depth=1)

# Build classpath
os.system(f'mvn install -Dmaven.test.skip')

expected_andy_version = 'v' + minidom.parse('pom.xml').getElementsByTagName('version')[0].firstChild.data

for category_dir in get_directories(home_dir):
    for assignment_dir in get_directories(category_dir):
        # Change to the assignment directory.
        os.chdir(assignment_dir)

        # Copy the solution to the correct folder.
        testfile = os.listdir(os.path.join(assignment_dir, 'solution'))[0]
        copyfile(f'{assignment_dir}/solution/{testfile}', f'{assignment_dir}/src/test/java/delft/{testfile}')

        # Run `andy` on the current assignment.
        output = os.popen('mvn andy:run -Dfull=true').read()
        re_score = re.search('Final grade: [0-9]+', output)
        score = int(re_score.group().split()[2]) if re_score else -1
        re_andy_version = re.search('Andy v.+', output)
        andy_version = re_andy_version.group() if re_andy_version else "Unknown Andy version"

        # Print the score for the assignment.
        print(f'{andy_version} | {assignment_dir.split("/")[-2]}/{assignment_dir.split("/")[-1]}: {score}/100')

        # Update the `pipeline_failed` variable.
        if score != 100:
            print(output)
            pipeline_failed = True

        if andy_version.startswith(expected_andy_version):
            print(f'Error: Unexpected Andy version {andy_version}, expected {expected_andy_version}')
            pipeline_failed = True

if pipeline_failed:
    sys.exit('Some assignments do not have 100/100.')
