package uk.gov.nationalarchives.pdi.step.xml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaSerializable;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.trans.RowStepCollector;
import org.pentaho.di.trans.TransMeta;

import org.pentaho.di.trans.TransTestFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CanonicalStepIT {

    static final String STEP_NAME = "Integration test for Canonical XML step";

    @BeforeAll
    public static void setUpBeforeClass() throws KettleException {
        KettleEnvironment.init(false);
    }

    @Test
    public void testSuccess() throws KettleException {
        final TransMeta tm = TransTestFactory.generateTestTransformationError(new Variables(), getTestMeta(), STEP_NAME);
        final Map<String, RowStepCollector> result = TransTestFactory.executeTestTransformationError(tm, TransTestFactory.INJECTOR_STEPNAME,
                STEP_NAME, TransTestFactory.DUMMY_STEPNAME, TransTestFactory.ERROR_STEPNAME, getValidInputData());
        assertEquals(0, result.get(STEP_NAME).getRowsError().size());
    }

    @Test
    public void testException() throws KettleException {
        final TransMeta tm = TransTestFactory.generateTestTransformationError(new Variables(), getTestMeta(), STEP_NAME);
        assertThrows(KettleException.class, () -> {
            TransTestFactory.executeTestTransformationError(tm, TransTestFactory.INJECTOR_STEPNAME,
                    STEP_NAME, TransTestFactory.DUMMY_STEPNAME, TransTestFactory.ERROR_STEPNAME, getInvalidInputData());
        });
    }

    private List<RowMetaAndData> getValidInputData() {
        final List<RowMetaAndData> retval = new ArrayList<>();
        final RowMetaInterface rowMeta = new RowMeta();
        rowMeta.addValueMeta(new ValueMetaSerializable("xml_string"));
        retval.add(new RowMetaAndData(rowMeta, "<doc>test &#38;</doc>"));
        return retval;
    }

    private List<RowMetaAndData> getInvalidInputData() {
        final List<RowMetaAndData> retval = new ArrayList<>();
        final RowMetaInterface rowMeta = new RowMeta();
        rowMeta.addValueMeta(new ValueMetaSerializable("xml_string"));
        retval.add(new RowMetaAndData(rowMeta, 123));
        return retval;
    }

    private CanonicalStepMeta getTestMeta() {
        final CanonicalStepMeta meta = new CanonicalStepMeta();
        meta.setInputField("xml_string");
        meta.setOutputField("canonical_xml");
        return meta;
    }
}
