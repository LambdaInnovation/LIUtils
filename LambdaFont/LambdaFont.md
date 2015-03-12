LambdaFont specification
=====
The font consists of two part: one or more .png file containing the actual image data of the font, and a .font file containing font size description and character mappings.

Basic Properties
=====
"_size" (int): font size in pixels. This is the size of each drawing block square.
"_charset_size"(int): How many characters this font contains. Used to derive texture resolution.
"_spacing" (int): Additional spacing between lines. Used to prevent space collision. Pixels in Additional space should be rendered as well.
"_maxlines" (int): How many lines that should be stored within one png.
"_file_%ID%" (string): The file name of the png file with id %ID%.

.png file
=====
the resolution of the file is always (_size * upper(_charset_size / 16), this_file_lines * 16). That is, every row contains exactly 16 characters.
If the character in a block is narrow, it is drawn in the left part of the block, else, it fills the whole block.
The offset of each character to the left-down origin of the block should be identical.

Mapping: row-col pair of index i is: (i / 16, i % 16), its tex mapping(_size * row, (_size + _spacing) * (col % _maxlines)) in file: floor(col / maxlines).

.lf file
=====
.lf file uses  properties-like representation, like:
```
[key]=[value]
```
When key begins with '_', it is a font property. Otherwise, it's a font mapping.

Font mapping:
```
[Char]=id,i,step
```
where id is the png id, i is the Mapping index mentioned above, and the step is the real x step of this character.

Undefined characters
=====
For undefined characters, always automatically assign to index to '?'. (This assumes that '?' must always be correctly mapped)
