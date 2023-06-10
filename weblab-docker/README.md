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

