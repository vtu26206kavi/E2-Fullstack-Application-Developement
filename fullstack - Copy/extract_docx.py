import docx
import sys

def read_docx(file_path):
    doc = docx.Document(file_path)
    content = []
    for i, p in enumerate(doc.paragraphs):
        text = p.text.strip()
        if text:
            content.append(text)
    
    with open('doc_content.txt', 'w', encoding='utf-8') as f:
        for c in content:
            f.write(c + '\n\n')

if __name__ == '__main__':
    read_docx(r'c:\Users\kavis\Downloads\cold_chain_IEEE_final.docx')
