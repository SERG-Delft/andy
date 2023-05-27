import os, re, sys
from shutil import copyfile
from xml.dom import minidom

def get_directories(basedir):
    return [os.path.join(basedir, dir) for dir in os.listdir(basedir) \
                                       if os.path.isdir(os.path.join(basedir, dir)) \
                                       and not dir.startswith('.')]

home_dir = os.path.join(os.getcwd(), 'assignments').replace("\\", "/")

expected_andy_version = 'v' + minidom.parse('pom.xml').getElementsByTagName('andy.version')[0].firstChild.data

maven_plugin_version = minidom.parse('andy-maven-plugin/pom.xml').getElementsByTagName('version')[1].firstChild.data

pipeline_failed = False
for category_dir in get_directories(home_dir):
    for assignment_dir in get_directories(category_dir):
        # Change to the assignment directory.
        os.chdir(assignment_dir)

        # Copy the solution to the correct folder.
        testfile = os.listdir(os.path.join(assignment_dir, 'solution'))[0]
        copyfile(f'{assignment_dir}/solution/{testfile}', f'{assignment_dir}/src/test/java/delft/{testfile}')

        # inject maven plugin version
        xmldoc = minidom.parse(f'{assignment_dir}/pom.xml')
        xml_version = xmldoc.getElementsByTagName('version')[3].firstChild
        xml_version.replaceWholeText(maven_plugin_version)
        with open(f'{assignment_dir}/pom.xml', 'w') as pom:
            xmldoc.writexml(pom)

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

        if expected_andy_version not in andy_version:
            print(f'Error: Unexpected Andy version {andy_version}, expected {expected_andy_version}')
            pipeline_failed = True

if pipeline_failed:
    sys.exit('Some assignments do not have 100/100.')
