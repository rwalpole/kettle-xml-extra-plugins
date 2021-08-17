package uk.gov.nationalarchives.pdi.step.xml;

import org.apache.commons.io.IOUtils;
import org.apache.xml.security.c14n.Canonicalizer;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

public class CanonicalStep extends BaseStep implements StepInterface {
    /**
     * This is the base step that forms that basis for all steps. You can derive from this class to implement your own
     * steps.
     *
     * @param stepMeta          The StepMeta object to run.
     * @param stepDataInterface the data object to store temporary data, database connections, caches, result sets,
     *                          hashtables etc.
     * @param copyNr            The copynumber for this step.
     * @param transMeta         The TransInfo of which the step stepMeta is part of.
     * @param trans             The (running) transformation to obtain information shared among the steps.
     */
    public CanonicalStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans) {
        super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }

    @Override
    public boolean processRow(final StepMetaInterface smi, final StepDataInterface sdi) throws KettleException {
        final CanonicalStepMeta meta = (CanonicalStepMeta) smi;
        final CanonicalStepData data = (CanonicalStepData) sdi;

        final Object[] row = getRow();
        if (row == null) {
            setOutputDone();
            return false;
        }

        if(first) {
            first = false;
            data.setOutputRowMeta(getInputRowMeta().clone());
            data.setOutputFieldIndex(data.getOutputRowMeta().indexOfValue(meta.getOutputField()));
            org.apache.xml.security.Init.init();
            data.setCanonicalizer(getCanonicalizer());
            data.setDocumentBuilder(getDocumentBuilder());
        }
        final Object xmlFieldValue = row[data.getXmlFieldIdx()];
        if(xmlFieldValue instanceof String) {
            final CanonicalizationResult result = process((String)xmlFieldValue,data);
            if(result.hasErrors()) {

            } else {
                result.getCanonicalXml();
            }
        } else {

        }

        return true;

    }

    private CanonicalizationResult process(String xmlString, CanonicalStepData data) {
        try{
            final Document xmlDoc = createDocument(xmlString,data);
            return new CanonicalizationResult(canonicalize(xmlDoc,data));
        } catch (KettleException kex) {
            CanonicalizationResult result = new CanonicalizationResult("");
            // TODO set error details
            return result;
        }
    }

    private Canonicalizer getCanonicalizer() throws KettleException {
        try {
            return Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_WITH_COMMENTS);
        } catch (Exception e) {
            throw new KettleException(e.getMessage(),e);
        }
    }

    private DocumentBuilder getDocumentBuilder() throws KettleException {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            return dbf.newDocumentBuilder();
        } catch (Exception e) {
            throw new KettleException(e.getMessage(), e);
        }
    }

    private Document createDocument(String xmlString, final CanonicalStepData data) throws KettleException {
        try{
            return data.getDocumentBuilder().parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            throw new KettleException(e.getMessage(),e);
        }
    }

    protected static String canonicalize(Document document, CanonicalStepData data) throws KettleException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            data.getCanonicalizer().canonicalizeSubtree(document, baos);
            return baos.toString();
        } catch (Exception e) {
            throw new KettleException(e.getMessage(),e);
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }
}
