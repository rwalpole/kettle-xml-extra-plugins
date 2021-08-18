package uk.gov.nationalarchives.pdi.step.xml;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.apache.xml.security.c14n.Canonicalizer;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class CanonicalStepTest {

    @Test
    public void testCanonicalize() throws Exception {
        org.apache.xml.security.Init.init();
        CanonicalStepData mockData = mock(CanonicalStepData.class);
        when(mockData.getCanonicalizer()).thenReturn(Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_WITH_COMMENTS));
        Document testXmlDoc = getTestDocument("<doc>&#8220;test&#8221;</doc>");
        String result = CanonicalStep.canonicalize(testXmlDoc, mockData);
        assertEquals("<doc>“test”</doc>",result);
    }

    private Document getTestDocument(final String xmlString) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlString)));
    }
}
