# Andy Docker image

This image is intended to be used on TU Delft's WebLab platform.

## Usage

The Makefile can be used to simplify the process of building and testing the image.

Note that the commands below require Docker privileges, i.e. you may have to run them with `sudo`.

### Building

`make`

### Testing

To run all tests:
`make test`

To run a specific test:
`make counting-clumps.test`

Requires the image to be built first.

Note that checking whether submissions lead to a 100% score is done by the "run_assignments_docker" CI step. 

The "tests" that are run when running `make test` in this directory do *not* necessarily have to result in 100/100.
