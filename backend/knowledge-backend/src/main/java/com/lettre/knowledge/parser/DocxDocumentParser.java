package com.lettre.knowledge.parser;


import com.lettre.knowledge.exception.BusinessException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import org.springframework.stereotype.Component;



@Component
public class DocxDocumentParser implements DocumentParser {


    @Override
    public String parse(Path filePath) {

        try (InputStream inputStream = Files.newInputStream(filePath);
             XWPFDocument document = new XWPFDocument(inputStream)) {

            return document.getParagraphs().stream()
                    .map(XWPFParagraph::getText)
                    .collect(Collectors.joining(System.lineSeparator()));

        } catch (Exception e) {

            throw new BusinessException(50013, "DOCX 文件解析失败");

        }

    }


    @Override
    public String supportedType() {

        return "docx";

    }


}
