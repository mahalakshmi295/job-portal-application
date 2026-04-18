"""
========================================================================
UTILITY MODULE FOR PDF GENERATION
========================================================================
Provides helper functions and utilities for PDF generation
"""

from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.units import pt, inch
from reportlab.lib.colors import HexColor, white
from reportlab.lib.enums import TA_CENTER, TA_LEFT, TA_JUSTIFY, TA_RIGHT
from reportlab.platypus import Table, TableStyle, Paragraph, Spacer, PageBreak, Preformatted
from PIL import Image, ImageDraw, ImageFont
import io

# Color constants
COLORS = {
    'primary': HexColor('#1F4788'),
    'secondary': HexColor('#CC0000'),
    'accent': HexColor('#F5F5F5'),
    'border': HexColor('#CCCCCC'),
    'text': HexColor('#333333'),
    'header': HexColor('#003366'),
    'light_gray': HexColor('#666666'),
    'white': white,
}

class StyleGenerator:
    """Generate custom paragraph styles"""
    
    @staticmethod
    def get_heading_style(level=1, size=18, color=COLORS['header']):
        """Get heading style"""
        return ParagraphStyle(
            name=f'Heading{level}',
            fontSize=size,
            textColor=color,
            spaceAfter=pt(12),
            spaceBefore=pt(12),
            fontName='Helvetica-Bold',
            alignment=TA_LEFT,
        )
    
    @staticmethod
    def get_body_style(size=11, color=COLORS['text']):
        """Get body text style"""
        return ParagraphStyle(
            name='Body',
            fontSize=size,
            textColor=color,
            spaceAfter=pt(6),
            alignment=TA_JUSTIFY,
            fontName='Helvetica',
        )
    
    @staticmethod
    def get_code_style(size=8):
        """Get code block style"""
        return ParagraphStyle(
            name='Code',
            fontSize=size,
            fontName='Courier',
            textColor=HexColor('#000000'),
            backColor=HexColor('#F5F5F5'),
            borderColor=COLORS['border'],
            borderWidth=1,
            borderPadding=8,
            leftIndent=12,
        )
    
    @staticmethod
    def get_table_caption_style():
        """Get table caption style"""
        return ParagraphStyle(
            name='TableCaption',
            fontSize=9,
            textColor=COLORS['text'],
            alignment=TA_CENTER,
            fontName='Helvetica-Oblique',
        )

class TableBuilder:
    """Build formatted tables"""
    
    @staticmethod
    def create_standard_table(data, col_widths=None, header_color=COLORS['header']):
        """Create a standard formatted table"""
        table = Table(data, colWidths=col_widths)
        table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, 0), header_color),
            ('TEXTCOLOR', (0, 0), (-1, 0), COLORS['white']),
            ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
            ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
            ('FONTSIZE', (0, 0), (-1, 0), 10),
            ('BOTTOMPADDING', (0, 0), (-1, 0), 12),
            ('GRID', (0, 0), (-1, -1), 1, COLORS['border']),
            ('FONTSIZE', (0, 1), (-1, -1), 9),
            ('ROWBACKGROUNDS', (0, 1), (-1, -1), [COLORS['white'], COLORS['accent']]),
            ('TOPPADDING', (0, 0), (-1, -1), 6),
            ('BOTTOMPADDING', (0, 0), (-1, -1), 6),
        ]))
        return table
    
    @staticmethod
    def create_comparison_table(data, col_widths=None):
        """Create a comparison table"""
        return TableBuilder.create_standard_table(data, col_widths, COLORS['primary'])
    
    @staticmethod
    def create_specification_table(data, col_widths=None):
        """Create a specification table"""
        return TableBuilder.create_standard_table(data, col_widths, COLORS['secondary'])

class ImageGenerator:
    """Generate placeholder images"""
    
    @staticmethod
    def create_placeholder_image(width=400, height=300, text="Diagram/Image", filename=None):
        """Create a placeholder image"""
        img = Image.new('RGB', (width, height), color=COLORS['accent'])
        draw = ImageDraw.Draw(img)
        
        # Draw border
        draw.rectangle([0, 0, width-1, height-1], outline='#333333', width=2)
        
        # Add text
        text_position = (width // 2, height // 2)
        draw.text(text_position, text, fill='#333333', anchor='mm')
        
        if filename:
            img.save(filename)
        
        return img

class ContentBuilder:
    """Build document content"""
    
    @staticmethod
    def create_section_header(title, styles):
        """Create a section header"""
        return [
            Spacer(1, 0.2 * inch),
            Paragraph(title, styles['CustomHeading2']),
            Spacer(1, 0.1 * inch),
        ]
    
    @staticmethod
    def create_subsection_header(title, styles):
        """Create a subsection header"""
        return [
            Spacer(1, 0.15 * inch),
            Paragraph(title, styles['CustomHeading3']),
            Spacer(1, 0.08 * inch),
        ]
    
    @staticmethod
    def create_bullet_list(items, styles):
        """Create a bullet list"""
        content = []
        for item in items:
            content.append(Paragraph(f"• {item}", styles['CustomBody']))
            content.append(Spacer(1, 0.05 * inch))
        return content
    
    @staticmethod
    def create_numbered_list(items, styles):
        """Create a numbered list"""
        content = []
        for i, item in enumerate(items, 1):
            content.append(Paragraph(f"{i}. {item}", styles['CustomBody']))
            content.append(Spacer(1, 0.05 * inch))
        return content

class CodeFormatter:
    """Format code blocks"""
    
    @staticmethod
    def format_code_with_line_numbers(code_text, style):
        """Format code with line numbers"""
        lines = code_text.split('\n')
        formatted = '\n'.join([f"{i+1:3d} | {line}" for i, line in enumerate(lines)])
        return Preformatted(formatted, style)
    
    @staticmethod
    def create_code_block(code_text, language="python"):
        """Create a formatted code block"""
        code_style = StyleGenerator.get_code_style()
        return CodeFormatter.format_code_with_line_numbers(code_text, code_style)

class PageElementBuilder:
    """Build page elements"""
    
    @staticmethod
    def create_page_break():
        """Create a page break"""
        return PageBreak()
    
    @staticmethod
    def create_spacer(height=0.1):
        """Create a spacer"""
        return Spacer(1, height * inch)
    
    @staticmethod
    def create_separator():
        """Create a horizontal separator"""
        return Paragraph(
            "_" * 100,
            ParagraphStyle(
                name='Separator',
                fontSize=10,
                textColor=COLORS['border'],
                alignment=TA_CENTER,
            )
        )

# ========================================================================
# Configuration Dictionary for Easy Access
# ========================================================================
PDF_CONFIG = {
    'page_size': 'A4',
    'margin_left': 1,  # inches
    'margin_right': 1,
    'margin_top': 1,
    'margin_bottom': 1,
    'colors': COLORS,
    'font_sizes': {
        'title': 28,
        'h1': 18,
        'h2': 14,
        'h3': 12,
        'body': 11,
        'small': 10,
        'footer': 9,
    }
}
