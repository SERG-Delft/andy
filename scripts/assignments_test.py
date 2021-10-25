import os, re, sys
from git import Repo
from shutil import copyfile

def get_directories(basedir):
    return [os.path.join(basedir, dir) for dir in os.listdir(basedir) \
                                       if os.path.isdir(os.path.join(basedir, dir)) \
                                       and not dir.startswith('.')]

# Compile Andy and store the `target` directory.
os.system('mvn clean compile dependency:copy-dependencies')
target_dir = '/home/runner/work/andy/andy/target'
home_dir = '/home/runner/work/andy/andy/assignments'

# Clone the assignments repository in a temporary directory.
Repo.clone_from('https://github.com/cse1110/assignments', home_dir)

# Set the environment variables.
os.environ['WORKING_DIR'] = os.path.join(os.getcwd(), 'code');
os.environ['OUTPUT_DIR']  = os.path.join(os.getcwd(), 'output');
os.makedirs(os.environ['WORKING_DIR'], exist_ok = True)
os.makedirs(os.environ['OUTPUT_DIR'],  exist_ok = True)

# Set the WebLab environment variables to not trigger an error.
os.environ['WL_COURSE'] = os.environ['WL_STUDENT'] = os.environ['WL_ASSIGNMENT_TITLE'] = ''

pipeline_failed = False
for category_dir in get_directories(home_dir):
    for assignment_dir in get_directories(category_dir):
        # Remove the contents of the output folder.
        os.system(f'rm -r {os.environ["OUTPUT_DIR"]}/*')

        # Copy the files to the correct folder.
        source = os.listdir(os.path.join(assignment_dir, 'src/main/java/delft'))[0]
        copyfile(f'{assignment_dir}/src/main/java/delft/{source}', os.path.join(os.environ['WORKING_DIR'], 'Library.java'))
        solution = os.listdir(os.path.join(assignment_dir, 'solution'))[0]
        copyfile(f'{assignment_dir}/solution/{solution}', os.path.join(os.environ['WORKING_DIR'], 'Solution.java'))
        copyfile(f'{assignment_dir}/config/Configuration.java', os.path.join(os.environ['WORKING_DIR'], 'Configuration.java'))

        # Run Andy and collect the results.
        os.system(f'java -ea -cp "{target_dir}/classes:{target_dir}/dependencies/*" nl.tudelft.cse1110.andy.AndyOnWebLab FULL_WITHOUT_HINTS {os.environ["WORKING_DIR"]} {os.environ["OUTPUT_DIR"]}')

        with open(f'{os.environ["OUTPUT_DIR"]}/stdout.txt') as file:
            # Get the score from the `stdout.txt` file.
            score = int(re.search('Final grade: [0-9]+', file.read()).group().split()[2])

            # Print the score for the assignment.
            print(f'{assignment_dir.split("/")[-2]}/{assignment_dir.split("/")[-1]}: {score}/100')

            # Update the `pipeline_failed` variable.
            pipeline_failed = pipeline_failed or (score != 100)

if pipeline_failed:
    sys.exit('Some assignments do not have 100/100.')
