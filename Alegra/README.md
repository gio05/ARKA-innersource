# Alegra integration

This script provides you the ability to obtain the detailed information about a list of products present in Alegra software (https://www.alegra.com/) and store it into a file.

## Pre-requisites

- Java 8+

## Usage

To call this script you can run the following command:

`` 
javac AlegraIntegration.java && java AlegraIntegration --firstProductId n --lastProductId m --token TOKEN
``

Where:

- firstProductId: the id of the initial product to be retrieved, the lower range. Default is 1.
- lastProductId: the id of the last product to be retrieved, the upper range. . Default is 1.
- token [**Mandatory**]: the Alegra token key required to call the Alegra API, this can be obtained from the Alegra console.
