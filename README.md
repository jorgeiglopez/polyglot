# README

Polyglot is an automated translator for internationalisation files based on AWS Translate service.

### Restrictions
A ) Reads JSON and writes JSON as output

B ) You need AWS credentials to call AWS Translate API

## How to use
1 - Go to the Configuration class and set the following:
- LOCALES_FOLDER_PATH: The path to the folder which contains all the various languages folder with their respective files
- SOURCE_LANGUAGE: The folder name (E.g.: "en" or "fr")
- FILE_NAME: The name of the JSON file that contains the keys and values to translate.
- TARGET_LANGUAGE: An array of all the target laguages

// TODO: include a list of supported languages

2 - Give a unique key to each word/phrase you want to translate, and set the value in the source language

3 - Run the main method. Ready!
