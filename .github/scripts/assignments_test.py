import os, re, sys
from shutil import copyfile
from xml.dom import minidom

def get_directories(basedir):
    return [os.path.join(basedir, dir) for dir in os.listdir(basedir) \
                                       if os.path.isdir(os.path.join(basedir, dir)) \
                                       and not dir.startswith('.')]

home_dir = '/home/runner/work/andy/andy/assignments'
andy_jar = '/home/runner/work/andy/andy.jar'

dir = os.path.join(os.getcwd(), 'work')
os.makedirs(dir, exist_ok = True)
os.chdir(dir)

expected_andy_version = 'v' + minidom.parse('pom.xml').getElementsByTagName('andy.version')[0].firstChild.data

pipeline_failed = False
for category_dir in get_directories(home_dir):
    for assignment_dir in get_directories(category_dir):

        # Remove the contents of the output folder.
        os.system(f'rm -r {dir}/*')

        # Copy the files to the correct folder.
        source = os.listdir(os.path.join(assignment_dir, 'src/main/java/delft'))[0]
        copyfile(f'{assignment_dir}/src/main/java/delft/{source}', os.path.join(dir, 'Library.java'))
        solution = os.listdir(os.path.join(assignment_dir, 'solution'))[0]
        copyfile(f'{assignment_dir}/solution/{solution}', os.path.join(dir, 'Solution.java'))
        copyfile(f'{assignment_dir}/config/Configuration.java', os.path.join(dir, 'Configuration.java'))

        # Run Andy and collect the results.
        os.system(f'java -ea -cp {andy_jar} -XX:+TieredCompilation -XX:TieredStopAtLevel=1 nl.tudelft.cse1110.andy.AndyLauncher "FULL_WITH_HINTS"')

        with open(f'{dir}/stdout.txt') as file:
            # Get the score from the `stdout.txt` file.
            file_content = file.read()
            re_score = re.search('Final grade: [0-9]+', file_content)
            score = int(re_score.group().split()[2]) if re_score else -1
            re_andy_version = re.search('Andy v.+', file_content)
            andy_version = re_andy_version.group() if re_andy_version else "Unknown Andy version"

            # Print the score for the assignment.
            print(f'{andy_version} | {assignment_dir.split("/")[-2]}/{assignment_dir.split("/")[-1]}: {score}/100')

            # Update the `pipeline_failed` variable.
            if score != 100:
                print(file_content)
                pipeline_failed = True

            if expected_andy_version not in andy_version:
                print(f'Error: Unexpected Andy version {andy_version}, expected {expected_andy_version}')
                pipeline_failed = True

if pipeline_failed:
    sys.exit('Some assignments do not have 100/100.')
