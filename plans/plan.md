# Plan MD

## rj notes

pre plan: known bug in original centroid finder logic will have to fix before completing. output is transparent.

CSV format:
seconds elapsed, x of largest centroid, y of largest centroid

if no centroid, return coord (-1,1)

need to run:

`java -jar videoprocessor.jar inputPath outputCsv targetColor threshold`

where:

`inputpath` = path to video
USE BOOF CV PROCESS VIDEOS AND PROCESS A FRAME FOR US TO USE

`outputCsv` = output csv file
PASS AS ARGUMENT IN FIND CONNECTED GROUPS

`targetcolor` = target color in 0xRRGGBB
PASS AS ARGUMENT IN DISTANCE IMAGE BINARIZER

`threshold` = allowed variance in color as a double
PASS AS ARGUMENT IN DISTANCE IMAGE BINARIZER


