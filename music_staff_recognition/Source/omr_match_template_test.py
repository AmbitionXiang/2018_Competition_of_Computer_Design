import sys
import cv2
import numpy as np

img = cv2.imread(sys.argv[1],0)
templatePath = sys.argv[2]

#templates = ('bass_clef_template.png','crotchet_rest_template.png','natural_template.png','quaver_rest_template.png','semibreve_rest_template.png','sharp_template.png','treble_clef_template.png')

templates = (
	('treble clef','treble_clef_template.png'),
	('bass clef','bass_clef_template.png'),
	('crotchet rest','crotchet_rest_template.png'),
	('time signature 4','time_signature_4_template.png'),
	('time signature 3','time_signature_3_template.png'),
	('quaver rest','quaver_rest_template.png'),
	('semibreve rest','semibreve_rest_template.png'),
	('sharp','sharp_template.png'),
	('natural','natural_template.png'),
	('flat','flat_template.png'),
	('note head','note_head_template.png'),
	('note head','note_head_2_template.png'),
	('minim note head','minim_note_head_template.png')
)

methods = [cv2.TM_CCOEFF,cv2.TM_CCOEFF_NORMED,cv2.TM_CCORR,cv2.TM_CCORR_NORMED,cv2.TM_SQDIFF,cv2.TM_SQDIFF_NORMED]

# objects is a dictionary with object names as keys and a list as the value. The first element of the list is a tuple containing the dimensions of the object and the second is a list of the locations of the objects
objects = {
	'treble clef': [],
	'bass clef': [],
	'crotchet rest': [],
	'time signature 4': [],
	'time signature 3': [],
	'quaver rest': [],
	'semibreve rest': [],
	'sharp': [],
	'natural': [],
	'flat': [],
	'note head': [],
	'minim note head': []
}

threshold = 0.7
for template in templates:
	templateImg = cv2.imread(templatePath + template[1],0)	
	height = len(templateImg)
	width = len(templateImg[1])
	
	result = cv2.matchTemplate(img,templateImg,methods[1])

	locations = np.where(result >= threshold)
	for point in zip(*locations[::-1]):
		cv2.rectangle(img,point,(point[0]+width,point[1]+height),255,-1)
		objects[template[0]].append((width,height))
		objects[template[0]].append([])
		objects[template[0][1]].append(point)

print(objects)

staffs = []



cv2.imwrite('template_match_test.png',img)
