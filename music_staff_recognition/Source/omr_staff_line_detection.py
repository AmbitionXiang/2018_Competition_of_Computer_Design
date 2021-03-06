import cv2
import numpy as np
import sys
from scipy import stats
import omr_classes

# Returns the mode vertical run length of the given colour in the input image
def verticalRunLengthMode(img,colour):
	runLengths = []
	width = len(img[0])
	height = len(img)
	for x in range(width*1/4,width*3/4):
		inColour = False
		currentRun = 0
		for y in range(0,height):
			if (img[y,x] == colour):
				if (inColour):
					currentRun = currentRun + 1
				else:
					currentRun = 1
					inColour = True
			else:
				if (inColour):
					runLengths.append(currentRun)
					inColour = False

	return int(stats.mode(runLengths)[0][0])

# Returns True iff rho corresponds to the top of a set of 5 staff lines
def isTopStaffLine(rho,rhoValues,gap,threshold):
	for i in range(1,5):
		member = False
		for j in range(0,threshold+1):
			if ((rho + i*gap + j) in rhoValues or (rho + i*gap - j) in rhoValues):
				member = True
		if (not(member)):
			return False
	return True

# Input: a thresholded sheet music image
# Output: an omr_classes.Staff object corresponding to the input
def getStaffData(imgInput):
	width = len(imgInput[0])
	height = len(imgInput)

	# Invert the input binary image
	imgBinary = 255 - imgInput

	# Output binary image
	parsedFilePath = sys.argv[1].split('/')
	imageName = parsedFilePath[-1].split('.')[0]
	cv2.imwrite('binary_output_' + imageName + '.png',imgBinary)

	# Apply Hough line transform
	houghLines = cv2.HoughLines(imgBinary,1,np.pi/180,min(width,height)/2)

	# Show lines on copy of input image
	imgHoughLines = imgInput.copy()

	for rho,theta in houghLines[0]:
		print('rho: ' + str(rho) + ' theta: ' + str(theta))
		cosTheta = np.cos(theta)
		sinTheta = np.sin(theta)
		if (sinTheta == 0):
			continue
		x0 = cosTheta*rho
		y0 = sinTheta*rho
		y1 = int(y0 + x0/np.tan(theta))
		y2 = int(y0 + (x0 - (width-1))/np.tan(theta))

		cv2.line(imgHoughLines,(0,y1),(width-1,y2),(0,0,255),1)
	"""
	for rho,theta in houghLines[0]:
		print('rho: ' + str(rho) + ' theta: ' + str(theta))
		cosTheta = np.cos(theta)
		sinTheta = np.sin(theta)
		x0 = cosTheta*rho
		y0 = sinTheta*rho
		length = max(width,height)
		x1 = int(x0 + length * (-sinTheta))
		y1 = int(y0 + length * (cosTheta))
		x2 = int(x0 - length * (-sinTheta))
		y2 = int(y0 - length * (cosTheta))

		cv2.line(imgHoughLines,(x1,y1),(x2,y2),(0,0,255),1)
	"""
	# Output image with Hough lines
	parsedFilePath = sys.argv[1].split('/')
	imageName = parsedFilePath[-1].split('.')[0]
	cv2.imwrite('hough_output_' + imageName + '.png',imgHoughLines)

	# Find most common black pixel run length (white pixel run length in binary image due to inversion)
	# This should correspond to staff line thickness

	staffLineThickness = verticalRunLengthMode(imgBinary,255)
	print("staffLineThickness: " + str(staffLineThickness))

	# Find staff line spacing

	# Find average difference in rho

	sortedHoughLines = sorted(houghLines[0],key = lambda x : x[0])

	"""
	rhoDifferences = np.zeros((len(sortedHoughLines)-1))
	for i in range(0,len(sortedHoughLines)-1):
		rhoDifferences[i] = sortedHoughLines[i+1][0] - sortedHoughLines[i][0]

	sortedRhoDifferences = sorted(rhoDifferences)

	medianRho = int(sortedRhoDifferences[len(sortedRhoDifferences)/2])

	print(sortedRhoDifferences)

	print("medianRho: " + str(medianRho))
	"""
	staffLineSpacing = verticalRunLengthMode(imgBinary,0) + staffLineThickness
	print("staffLineSpacing: " + str(staffLineSpacing))
	# Now we keep only lines with theta between 1.56 and 1.58. We also only store rho values from now on

	sortedRhoValues = []
	for rho,theta in sortedHoughLines:
		if (1.56 < theta and theta < 1.58):
			sortedRhoValues.append(int(rho))

	print("sortedRhoValues: " + str(sortedRhoValues))

	# Find rho value of top line of each stave
	staffTops = []

	for rho in sortedRhoValues:
		if (isTopStaffLine(rho,sortedRhoValues,staffLineSpacing,1)):
			staffTops.append(rho)

	print("staffTops: " + str(staffTops))

	# Show staff lines on copy of input image
	imgStaffLines = imgInput.copy()
	imgStaffLines = cv2.cvtColor(imgStaffLines,cv2.COLOR_GRAY2RGB)
	for rho in staffTops:
		for i in range(0,5):
			y = rho + i*staffLineSpacing
			cv2.line(imgStaffLines,(0,y),(width-1,y),(0,0,255),1)

	# Output image with staff lines
	parsedFilePath = sys.argv[1].split('/')
	imageName = parsedFilePath[-1].split('.')[0]
	cv2.imwrite('staff_output_' + imageName + '.png',imgStaffLines)

	currentStaff = omr_classes.Staff(staffLineSpacing,staffLineThickness,staffTops)
	return currentStaff
