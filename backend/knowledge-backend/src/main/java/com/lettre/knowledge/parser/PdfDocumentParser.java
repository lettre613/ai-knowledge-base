package com.lettre.knowledge.parser;


import com.lettre.knowledge.exception.BusinessException;

import java.nio.file.Path;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.stereotype.Component;



@Component
public class PdfDocumentParser implements DocumentParser {


    @Override
    public String parse(Path filePath) {

        try (PDDocument document = Loader.loadPDF(filePath.toFile())) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);

        } catch (Exception e) {

            throw new BusinessException(50012, "PDF 文件解析失败");

        }

    }


    @Override
    public String supportedType() {

        return "pdf";

    }


}
