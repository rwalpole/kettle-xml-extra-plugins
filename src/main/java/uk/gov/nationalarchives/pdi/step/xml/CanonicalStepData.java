package uk.gov.nationalarchives.pdi.step.xml;

import org.apache.xml.security.c14n.Canonicalizer;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

import javax.xml.parsers.DocumentBuilder;

public class CanonicalStepData extends BaseStepData implements StepDataInterface {

    private Canonicalizer canon;
    private DocumentBuilder builder;
    private RowMetaInterface outputRowMeta;
    private int xmlFieldIdx;

    public void setXmlFieldIdx(int xmlFieldIdx) {
        this.xmlFieldIdx = xmlFieldIdx;
    }

    public int getOutputFieldIndex() {
        return outputFieldIndex;
    }

    public void setOutputFieldIndex(int outputFieldIndex) {
        this.outputFieldIndex = outputFieldIndex;
    }

    private int outputFieldIndex;

    public void setCanonicalizer(Canonicalizer canon) {
        this.canon = canon;
    }

    public Canonicalizer getCanonicalizer() {
        return canon;
    }

    public void setOutputRowMeta(RowMetaInterface outputRowMeta) {
        this.outputRowMeta = outputRowMeta;
    }

    public RowMetaInterface getOutputRowMeta() {
        return outputRowMeta;
    }

    public void setDocumentBuilder(DocumentBuilder builder) {
        this.builder = builder;
    }

    public DocumentBuilder getDocumentBuilder() {
        return builder;
    }

    public int getXmlFieldIdx() {
        return xmlFieldIdx;
    }
}
