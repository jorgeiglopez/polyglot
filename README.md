# README: Polyglot

Polyglot is an automated translator for internationalisation files based on AWS Translate service.

### Restrictions

- Reads JSON and writes JSON as output

- You need AWS credentials to call AWS Translate API

---------

## How to use

### 1) Configuration

Go to `ConfigGenerator` to configure:

A - Source language (English default)

B - Target languages

C - File names and project folders

D - Translation exceptions for unsupported languages (en-US and es-MX)

### 2) AWS Credentials

You should have your AWS credentials configured already.

If your account uses SSO, no changes are required. Just update `ConfigGenerator` the variable `AWS_PROFILE_NAME`.

If You use a different authentication method, replace the crendetials provider in the class `ClientProvider`

E - Then login by executing: `aws sso login --profile=<PROFILE_NAME>`

### 3) Execute translations

F - Go to `Main` and execute the main() method.
