import os, re, sys

def get_directories(basedir):
    return [os.path.join(basedir, dir) for dir in os.listdir(basedir) \
                                       if os.path.isdir(os.path.join(basedir, dir)) \
                                       and not dir.startswith('.')]

home_dir = '/home/runner/work/andy/andy/assignments'
output_dir = '/home/runner/work/andy/andy/weblab-docker-v2/testresults'
test_dir = '/home/runner/work/andy/andy/weblab-docker-v2/tests/github-ci'

commit_hash = os.environ['COMMIT_HASH']
print(f'Running Andy on {commit_hash}')

expected_andy_version = f'-{commit_hash[:7]} '

# Switch to Docker directory
os.chdir('./weblab-docker-v2')

# Create test structure
os.makedirs(test_dir)

pipeline_failed = False
for category_dir in get_directories(home_dir):
    for assignment_dir in get_directories(category_dir):
        # Remove the contents of the output directory.
        os.system(f'rm -r {output_dir}')

        # Remove the contents of the test directory.
        os.system(f'rm -r {test_dir}/*')

        # Copy the assignment to the test folder.
        os.system(f'cp {assignment_dir}/config/Configuration.java {test_dir}/test.txt')
        os.system(f'cp {assignment_dir}/solution/*.java {test_dir}/solution.txt')
        os.system(f'cp {assignment_dir}/src/main/java/delft/*.java {test_dir}/library.txt')

        # Run `andy` on the current assignment.
        output = os.popen('make github-ci.test').read()

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
