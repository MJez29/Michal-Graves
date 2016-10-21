from simplexnoise import *

img = Surface((50, 20))

freq = 1 / 10

for x in range(50):
    for y in range(20):
        col = int((octave_noise_2d(5, 1 / 4, 1 / 2, x * freq, y * freq) + 1) / 2 * 51)
        img.set_at((x,y), (0,col * 5,0))

image.save(img, "Grass.png")
print("DONE")
