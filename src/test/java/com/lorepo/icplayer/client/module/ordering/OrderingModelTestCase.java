package com.lorepo.icplayer.client.module.ordering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.SAXException;

import com.google.gwt.xml.client.Element;
import com.lorepo.icf.properties.IHtmlProperty;
import com.lorepo.icf.properties.IListProperty;
import com.lorepo.icf.properties.IProperty;
import com.lorepo.icf.properties.IPropertyListener;
import com.lorepo.icf.properties.IPropertyProvider;
import com.lorepo.icf.utils.i18n.DictionaryWrapper;
import com.lorepo.icplayer.client.mockup.xml.XMLParserMockup;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DictionaryWrapper.class)
public class OrderingModelTestCase {

	private boolean eventReceived;

	@Test
	public void moduleTypeName() {
		PowerMockito.spy(DictionaryWrapper.class);
		when(DictionaryWrapper.get("ordering_module")).thenReturn("Ordering");

		OrderingModule module = new OrderingModule();
		assertEquals("Ordering", module.getModuleTypeName());
	}

	
	@Test
	public void propertyEvent() {
		
		OrderingModule module = new OrderingModule();
		
		eventReceived = false;
		module.addPropertyListener(new IPropertyListener() {
			
			@Override
			public void onPropertyChanged(IProperty source) {
				eventReceived = true;
			}
		});
		
		// Look for property "Item 1"
		for(int i = 0; i < module.getPropertyCount(); i++){
			
			if(module.getProperty(i) instanceof IListProperty){
				IListProperty listProperty = (IListProperty) module.getProperty(i);
				IPropertyProvider propertyProvider = listProperty.getChild(0);
				for(int j = 0; j < propertyProvider.getPropertyCount(); j++){
					IProperty property = propertyProvider.getProperty(j); 
					if(propertyProvider.getProperty(j) instanceof IHtmlProperty){
						property.setValue("test");
						break;
					}
				}
				break;
			}
		}
		
		assertTrue(eventReceived);
	}


	@Test
	public void propertyAddItems() {
		
		OrderingModule module = new OrderingModule();
		
		int count = 0;
		
		for(int i = 0; i < module.getPropertyCount(); i++){
			
			if(module.getProperty(i) instanceof IListProperty){
				IListProperty listProperty = (IListProperty) module.getProperty(i);
				listProperty.addChildren(5);
				count = listProperty.getChildrenCount();
				break;
			}
		}
		
		assertEquals(8, count);
	}
	
	@Test
	public void saveLoad() throws SAXException, IOException {
		
		InputStream inputStream = getClass().getResourceAsStream("testdata/ordering2.xml");
		XMLParserMockup xmlParser = new XMLParserMockup();
		Element element = xmlParser.parser(inputStream);
		
		OrderingModule module = new OrderingModule();
		module.load(element, "");
		String oldText = module.getItem(0).getText();
				
		String xml = module.toXML();
		element = xmlParser.parser(new StringInputStream(xml));
		module = new OrderingModule();
		module.load(element, "");
		String newText = module.getItem(0).getText();
		
		assertEquals(oldText, newText);
		assertFalse(module.isActivity());
	}


	@Test
	public void goToPageProperty() throws SAXException, IOException {
		
		InputStream inputStream = getClass().getResourceAsStream("testdata/ordering.xml");
		XMLParserMockup xmlParser = new XMLParserMockup();
		Element element = xmlParser.parser(inputStream);
		
		OrderingModule module = new OrderingModule();
		module.load(element, "");
		String xml = module.toXML();
		element = xmlParser.parser(new StringInputStream(xml));
		module = new OrderingModule();
		module.load(element, "");
		
		assertEquals(4, module.getItemCount());
	}


	@Test
	public void optionalOrder() throws SAXException, IOException {
		
		PowerMockito.spy(DictionaryWrapper.class);
		when(DictionaryWrapper.get("optional_order")).thenReturn("order");
		
		InputStream inputStream = getClass().getResourceAsStream("testdata/ordering.xml");
		XMLParserMockup xmlParser = new XMLParserMockup();
		Element element = xmlParser.parser(inputStream);
		
		OrderingModule module = new OrderingModule();
		module.load(element, "");
		
		boolean foundProperty = false;
		for(int i = 0; i < module.getPropertyCount(); i++){
			
			IProperty property = module.getProperty(i);
			if(property.getName().compareTo("order") == 0){
				foundProperty = true;
			}
		}

		assertTrue(foundProperty);
	}

	@Test
	public void propertyisActivity() {
		
		PowerMockito.spy(DictionaryWrapper.class);
		when(DictionaryWrapper.get("is_activity")).thenReturn("Is&nbsp;activity");

		OrderingModule module = new OrderingModule();
		boolean foundProperty = false;
		for(int i = 0; i < module.getPropertyCount(); i++){
			
			IProperty property = module.getProperty(i);
			if(property.getName().compareTo("Is&nbsp;activity") == 0){
				foundProperty = true;
			}
		}

		assertTrue(foundProperty);
	}
}
