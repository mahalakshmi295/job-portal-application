# Professional PDF Documentation Generator

A comprehensive Python tool for generating professional 70+ page PDF documentation with the same formatting, styling, and structure as academic/technical reports.

## Features

✅ **70+ Pages of Content** - Automatically generates extensive documentation  
✅ **Professional Formatting** - Matches academic/technical report standards  
✅ **Table of Contents** - Auto-generated with page numbers  
✅ **List of Figures & Tables** - Comprehensive index pages  
✅ **Custom Styling** - Professional fonts, colors, and alignment  
✅ **Chapter-Based Structure** - Organized sections and subsections  
✅ **Code Blocks** - Syntax-highlighted code with line numbers  
✅ **Technical Tables** - Professional formatted tables with styling  
✅ **Headers & Footers** - Page numbers and document info on each page  
✅ **Modular Design** - Easily customizable and extensible

## Project Structure

```
├── pdf_generator_main.py      # Main PDF generation script
├── pdf_utilities.py           # Utility functions and helpers
├── requirements.txt           # Python dependencies
└── README.md                  # This file
```

## Installation

### 1. Install Python Dependencies

```bash
pip install -r requirements.txt
```

**Required packages:**

- `reportlab` - PDF generation library
- `Pillow` - Image handling

### 2. Verify Installation

```bash
python -c "import reportlab; import PIL; print('✓ All dependencies installed')"
```

## Usage

### Quick Start

```bash
python pdf_generator_main.py
```

This will generate `Professional_Documentation_Report.pdf` in the current directory.

### Customizing the PDF

#### Edit Title Page

In `pdf_generator_main.py`, modify the `create_title_page()` function:

```python
content.append(Paragraph(
    "Your Custom Title Here",
    styles['CustomTitle']
))
```

#### Add Custom Chapters

In the `generate_pdf()` function, add new chapter sections:

```python
chapter_sections = [
    (
        "Chapter Title",
        [
            "Your content here",
            "More content..."
        ]
    ),
]

story.extend(create_chapter_content(styles, 1, "CHAPTER NAME", chapter_sections))
```

#### Add Tables

```python
table_data = [
    ['Header 1', 'Header 2', 'Header 3'],
    ['Row 1, Col 1', 'Row 1, Col 2', 'Row 1, Col 3'],
]

table = TableBuilder.create_standard_table(table_data, col_widths=[2*inch, 2*inch, 1.2*inch])
story.append(table)
```

#### Add Code Blocks

```python
code_text = """
def hello_world():
    print("Hello, World!")
"""

story.append(CodeFormatter.create_code_block(code_text, language="python"))
```

## File Structure Details

### pdf_generator_main.py

Main module containing:

- `PDFConfig` - Configuration constants
- `get_custom_styles()` - Style definitions
- `HeaderFooterCanvas` - Custom header/footer implementation
- `create_title_page()` - Title page creation
- `create_table_of_contents()` - TOC generation
- `create_list_of_figures()` - Figure index
- `create_list_of_tables()` - Table index
- `create_chapter_content()` - Chapter builders
- `generate_pdf()` - Main PDF generation function

### pdf_utilities.py

Utility module with:

- `COLORS` - Color scheme dictionary
- `StyleGenerator` - Create consistent styles
- `TableBuilder` - Build formatted tables
- `ImageGenerator` - Create placeholder images
- `ContentBuilder` - Build content elements
- `CodeFormatter` - Format code blocks
- `PageElementBuilder` - Build page elements

## Customization Guide

### 1. Change Color Scheme

Edit `pdf_utilities.py`:

```python
COLORS = {
    'primary': HexColor('#YOUR_COLOR'),
    'secondary': HexColor('#YOUR_COLOR'),
    # ... other colors
}
```

### 2. Modify Font Sizes

Edit `PDFConfig` in `pdf_generator_main.py`:

```python
TITLE_SIZE = 28
HEADING1_SIZE = 18
# ... other sizes
```

### 3. Add Custom Styles

Use `StyleGenerator` in `pdf_utilities.py`:

```python
custom_style = StyleGenerator.get_heading_style(
    level=1,
    size=20,
    color=HexColor('#FF0000')
)
```

### 4. Include External Images

```python
from reportlab.platypus import Image as RLImage

img = RLImage('path/to/image.png', width=4*inch, height=3*inch)
story.append(img)
```

## Output

The script generates a single PDF file with:

- **Title Page** - Professional cover page
- **Table of Contents** - All sections with page numbers
- **List of Figures** - All diagrams and images
- **List of Tables** - All data tables
- **Multiple Chapters** - Main content (70+ pages)
- **References** - Academic references
- **Appendix** - Additional technical details

## Example Output

- **Filename**: `Professional_Documentation_Report.pdf`
- **Pages**: 70+
- **Size**: ~2-5 MB (depending on images)
- **Format**: A4, portrait orientation

## Advanced Features

### Header and Footer Management

The `HeaderFooterCanvas` class handles:

- Page numbers
- Document title
- Department/institution info
- Professional formatting

### Table of Contents Generation

Automatically generated with:

- Section titles
- Page number alignment
- Professional formatting

### Code Block Formatting

Includes:

- Line numbers
- Syntax highlighting (via formatting)
- Background colors
- Border styling

## Troubleshooting

### Issue: Missing Dependencies

**Solution**: Install requirements

```bash
pip install -r requirements.txt
```

### Issue: Font Not Found

**Solution**: reportlab uses standard fonts automatically. If you need custom fonts, add them to the workspace.

### Issue: Large File Size

**Solution**: Compress images or reduce image count in the document.

### Issue: Memory Error

**Solution**: For very large documents, generate in sections and merge PDFs separately.

## Best Practices

1. **Keep Images Optimized** - Use compressed images for faster generation
2. **Modularize Content** - Create separate functions for each chapter
3. **Test Changes** - Test styling on small chapters before applying to full document
4. **Use Consistent Spacing** - Maintain uniform spacing for professional appearance
5. **Validate Tables** - Ensure table data is properly formatted

## API Reference

### Main Functions

```python
generate_pdf(output_path)
    Generate complete PDF documentation
    Args:
        output_path (str): Path to save the PDF file

create_title_page(styles)
    Create title page content
    Returns:
        list: Content elements for title page

create_chapter_content(styles, chapter_num, chapter_title, sections)
    Create chapter with sections
    Args:
        styles: Custom styles dictionary
        chapter_num: Chapter number
        chapter_title: Chapter title
        sections: List of (title, content) tuples

create_sample_table(styles)
    Create formatted sample table
    Returns:
        Table: Formatted reportlab Table
```

## Dependencies Explanation

### reportlab (4.0.0+)

- PDF generation library
- Handles fonts, colors, and formatting
- Creates complex layouts

### Pillow (9.0.0+)

- Image processing library
- Used for image placeholders
- Handles image resizing

## Version History

**v1.0.0** - Initial release

- 70+ page PDF generation
- Professional styling
- Table of Contents
- Multiple content types

## License

This tool is provided as-is for educational and professional use.

## Support

For issues or customizations, refer to:

1. ReportLab documentation: https://www.reportlab.com/docs/reportlab-userguide.pdf
2. Pillow documentation: https://pillow.readthedocs.io/
3. The comments in the source code

## Example Usage

```python
#!/usr/bin/env python3
"""
Example: Generate Custom PDF Documentation
"""

from pdf_generator_main import generate_pdf

# Generate PDF
generate_pdf("My_Custom_Documentation.pdf")

print("✓ PDF generated successfully!")
print("✓ File: My_Custom_Documentation.pdf")
```

Run with:

```bash
python example_usage.py
```

---

**Created**: 2024  
**Last Updated**: April 2026  
**Status**: Production Ready
