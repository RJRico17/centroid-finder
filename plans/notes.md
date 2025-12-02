# RJ notes
Image summary map = three line command to make image into black and white
path to image
target color
threshold (margin of error)

## process initial args
@31:
if less than three args, print correction statement
store args into strings and integer, try to convert integer first before input

## proccess image
@48:
make new BufferedImage object, try read image file with new File(image arg)
else, return system error

## process target color
@58: 
default target color 0, turn into 24 bit integer
run thru parsInt(color arg, 16) 16 for base 16

# @66 alot
basically, run a new distancefinder object which finds the distance between two colors
make the image a 2d array of every pixel with 1 being white and 0 black
binarizer calculates if the pixel is close enough to be white or black
@74:
try making new image binary, print new image based off black or white, print success message, else print err

# find grouping in picture
@83:
run a new imagegroupfinder to find groups of white pixels and return them as a list, write them to csv

Rudolf's Notes
## Image summary APP
Convert hex string into 24-bit integer
Learn about the Euclidean color distance; if a color within the image is within the threshold of the target color, than it turns white, otherwise turn black white (1), black(0).

Convert binary array back ot a bufferedImage (binarized.png). 
pixels are not connected diagonally (only vertically / horizontally)


END

