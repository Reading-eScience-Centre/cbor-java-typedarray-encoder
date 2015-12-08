package uk.ac.rdg.resc.cbor.typedarrays.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.List;

import org.junit.Test;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.UnicodeString;

public class TypedArrayTest {

    @Test
    public void shouldEncode() throws CborException {
    	// encode
    	int[] arr = {1,2,3};
    	
    	Map root = new Map();
    	root.put(new UnicodeString("values"), new TypedArray(arr));
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	new CborEncoder(baos).encode(root);
    	
    	// decode
    	ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    	List<DataItem> items = new CborDecoder(bais).decode();
    	assertEquals(1, items.size());
    	assertEquals(Map.class, items.get(0).getClass());
    	Map decodedRoot = (Map) items.get(0);
    	assertEquals(1, decodedRoot.getKeys().size());
    	DataItem vals = decodedRoot.get(new UnicodeString("values"));
    	assertTrue(vals instanceof ByteString);
    	// these are raw ints encoded as little endian ordered bytes
    	byte[] bytes = ((ByteString)vals).getBytes();
    	ByteBuffer bb = ByteBuffer.wrap(bytes);
    	bb.order(ByteOrder.LITTLE_ENDIAN);
    	IntBuffer intbuf = bb.asIntBuffer();
    	for (int i=0; i < arr.length; i++) {
    		assertEquals(arr[i], intbuf.get(i));
    	}
    }
	
}
