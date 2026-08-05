package com.lettre.knowledge.parser;


import java.nio.file.Path;

import org.springframework.stereotype.Component;



@Component
public class MdDocumentParser implements DocumentParser {


    private final TxtDocumentParser txtDocumentParser;


    public MdDocumentParser(TxtDocumentParser txtDocumentParser) {
        this.txtDocumentParser = txtDocumentParser;
    }


    @Override
    public String parse(Path filePath) {

        return txtDocumentParser.parse(filePath);

    }


    @Override
    public String supportedType() {

        return "md";

    }


}
