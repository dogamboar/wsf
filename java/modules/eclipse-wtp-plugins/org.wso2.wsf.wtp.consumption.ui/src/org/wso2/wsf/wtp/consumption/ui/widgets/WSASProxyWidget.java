/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.wsf.wtp.consumption.ui.widgets;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;

import org.apache.axis2.util.URLProcessor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.wso2.wsf.wtp.consumption.core.data.DataModel;
import org.wso2.wsf.wtp.consumption.core.messages.WSASConsumptionUIMessages;
import org.wso2.wsf.wtp.consumption.core.utils.WSDLPropertyReader;

public class WSASProxyWidget extends SimpleWidgetDataContributor {
	
	DataModel model;
	IStatus status = Status.OK_STATUS;


	public WSASProxyWidget(DataModel model) {
		this.model=model;
	}

	//private String pluginId_ = "org.eclipse.jst.ws.axis2.consumption.ui";
	
	private Button genProxyCheckbox_;
	private Button syncAndAsyncRadioButton;
	private Button syncOnlyRadioButton;
	private Button asyncOnlyRadioButton;
	//private Button testCaseCheckBoxButton;
	//private Button generateAllCheckBoxButton;
	private Combo databindingTypeCombo;
	private Combo portNameCombo;
	private Combo serviceNameCombo;
	private Text packageText;
	Label   label, fillLabel, fillLabel1, fillLabel2, fillLabel3, fillLabel4, fillLabel5, fillLabel6;
	//private java.util.List serviceQNameList2 = null;
	private Table namespace2packageTable = null;
	private Button generateAllCheckBoxButton;
	private Button testCaseCheckBoxButton;
	
	private WSDLPropertyReader reader;
	private List serviceQNameList = null;


	public WidgetDataEvents addControls( Composite parent, Listener statusListener )
	{
		
		Composite  mainComp = new Composite( parent, SWT.NONE );
		GridLayout layout   = new GridLayout();
		mainComp.setLayout(layout);

		layout.numColumns = 3;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		mainComp.setLayoutData( gd );

		// service name
		label = new Label(mainComp, SWT.NULL);
		label.setText(WSASConsumptionUIMessages.LABEL_SERVICE_NAME_CAPTION);

		serviceNameCombo = new Combo(mainComp, SWT.DROP_DOWN | SWT.BORDER
				| SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		serviceNameCombo.setLayoutData(gd);
		serviceNameCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//loadPortNames();
				model.setServiceName(serviceNameCombo.getText());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// port name
		label = new Label(mainComp, SWT.NULL);
		label.setText(WSASConsumptionUIMessages.LABEL_PORTNAME);
		portNameCombo = new Combo(mainComp, SWT.DROP_DOWN | SWT.BORDER
				| SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		portNameCombo.setLayoutData(gd);
		portNameCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setPortName(portNameCombo.getText());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// Databinding
		label = new Label(mainComp, SWT.NULL);
		label.setText(WSASConsumptionUIMessages.LABEL_DATABINDING_CAPTION);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		databindingTypeCombo = new Combo(mainComp, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		databindingTypeCombo.setLayoutData(gd);
		fillDatabinderCombo();
		databindingTypeCombo.select(0);
		databindingTypeCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setDatabindingType(databindingTypeCombo.getText());
			};
			public void widgetDefaultSelected(SelectionEvent e) {
			};
		});
		
		// generate test case option
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		testCaseCheckBoxButton = new Button(mainComp, SWT.CHECK);
		testCaseCheckBoxButton.setLayoutData(gd);
		testCaseCheckBoxButton
		.setText(WSASConsumptionUIMessages.LABEL_GENERATE_TESTCASE_CAPTION);
		model.setTestCaseCheck(false);
		testCaseCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setTestCaseCheck(true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});


		// generate all
		generateAllCheckBoxButton = new Button(mainComp, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		generateAllCheckBoxButton.setLayoutData(gd);
		generateAllCheckBoxButton.setSelection(false);
		generateAllCheckBoxButton.setText(WSASConsumptionUIMessages.LABEL_GENERATE_ALL);
		generateAllCheckBoxButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setGenerateAllCheck(true);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// package name
		label = new Label(mainComp, SWT.NULL);
		label.setText(WSASConsumptionUIMessages.LABEL_PACKEGE_NAME);
		packageText = new Text(mainComp, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;

		packageText.setLayoutData(gd);
		packageText.setText(""); // get this text from the
		packageText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				model.setPackageText(packageText.getText());
			}
		});
		
		//filling label 
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		fillLabel = new Label(mainComp, SWT.HORIZONTAL | SWT.NULL);
		fillLabel.setLayoutData(gd);
		
		//filling label 
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		fillLabel2 = new Label(mainComp, SWT.HORIZONTAL | SWT.NULL);
		fillLabel2.setText(WSASConsumptionUIMessages.LABEL_CLIENT_SIDE);
		fillLabel2.setLayoutData(gd);
		
		//client side buttons
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		syncAndAsyncRadioButton = new Button(mainComp, SWT.RADIO);
		syncAndAsyncRadioButton.setLayoutData(gd);
		syncAndAsyncRadioButton.setText(WSASConsumptionUIMessages.LABEL_SYNC_AND_ASYNC);
		syncAndAsyncRadioButton.setVisible(true);
		syncAndAsyncRadioButton.setSelection(true);
		syncAndAsyncRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setSync(syncOnlyRadioButton.getSelection());
				model.setSync(asyncOnlyRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		syncOnlyRadioButton = new Button(mainComp, SWT.RADIO);
		syncOnlyRadioButton.setLayoutData(gd);
		syncOnlyRadioButton.setText(WSASConsumptionUIMessages.LABEL_SYNC);
		syncOnlyRadioButton.setSelection(false);
		syncOnlyRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setSync(syncOnlyRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		asyncOnlyRadioButton = new Button(mainComp, SWT.RADIO);
		asyncOnlyRadioButton.setLayoutData(gd);
		asyncOnlyRadioButton
				.setText(WSASConsumptionUIMessages.LABEL_ASYNC);
		asyncOnlyRadioButton.setSelection(false);
		asyncOnlyRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				model.setSync(asyncOnlyRadioButton.getSelection());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		//filling label 
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		fillLabel3 = new Label(mainComp, SWT.HORIZONTAL | SWT.NULL);

		//filling label 
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		fillLabel4 = new Label(mainComp, SWT.HORIZONTAL | SWT.NULL);
		
		//add a table to set namespace to package mapping
		gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        gd.verticalSpan = 5;
        
        namespace2packageTable = new Table(mainComp,SWT.BORDER|SWT.MULTI);
        namespace2packageTable.setLinesVisible(true);
        namespace2packageTable.setHeaderVisible(true); 
        namespace2packageTable.setEnabled(true);
        namespace2packageTable.setLayoutData(gd);
       
        declareColumn(namespace2packageTable,
        		350, //a default width until we adjust
        		WSASConsumptionUIMessages.LABEL_NAMESPACE);
        declareColumn(namespace2packageTable,
        		200,//a default width until we adjust
        		WSASConsumptionUIMessages.LABEL_PACKAGE);
        
        namespace2packageTable.setVisible(true);
        
        // add the table editor
        final TableEditor editor = new TableEditor(namespace2packageTable);
        editor.setColumn(1);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        namespace2packageTable.addListener(SWT.MouseDown, new Listener() {
            public void handleEvent(Event event) {
              Rectangle clientArea = namespace2packageTable.getClientArea();
              Point pt = new Point(event.x, event.y);
              int index = namespace2packageTable.getTopIndex();
              while (index < namespace2packageTable.getItemCount()) {
                boolean visible = false;
                final TableItem item = namespace2packageTable.getItem(index);
                for (int i = 0; i < namespace2packageTable.getColumnCount(); i++) {
                  Rectangle rect = item.getBounds(i);
                  if (rect.contains(pt)) {
                    final int column = i;
                    final Text text = new Text(namespace2packageTable, SWT.NONE);
                    Listener textListener = new Listener() {
                      public void handleEvent(final Event e) {
                        switch (e.type) {
                        case SWT.FocusOut:
                          item.setText(column, text.getText());
                          text.dispose();
                          break;
                        case SWT.Traverse:
                          switch (e.detail) {
                          case SWT.TRAVERSE_RETURN:
                            item
                                .setText(column, text
                                    .getText());
                          // FALL THROUGH
                          case SWT.TRAVERSE_ESCAPE:
                            text.dispose();
                            e.doit = false;
                          }
                          break;
                        }
                      }
                    };
                    text.addListener(SWT.FocusOut, textListener);
                    text.addListener(SWT.Traverse, textListener);
                    editor.setEditor(text, item, i);
                    text.setText(item.getText(i));
                    text.selectAll();
                    text.setFocus();
                    return;
                  }
                  if (!visible && rect.intersects(clientArea)) {
                    visible = true;
                  }
                }
                if (!visible)
                  return;
                index++;
              }
              model.setNamespaseToPackageMapping(getNs2PkgMapping());
            }
          });
        

//		UIUtils      uiUtils  = new UIUtils( pluginId_ );
//
//		parent.setToolTipText( WSASConsumptionUIMessages.TOOLTIP_PPAE_PAGE );
//		PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." +  this.pluginId_);
		
		populateParamsFromWSDL();
		populateModel();

		return this;
	}
	
	/**
	 * A util method to create a new column
	 * @param table
	 * @param width
	 * @param colName
	 */
	private void declareColumn(Table table, int width,String colName){
        TableColumn column = new TableColumn(table,SWT.NONE);
        column.setWidth(width);
        column.setText(colName);
    }
	
	/**
	 * get the package to namespace mappings
	 * @return
	 */
	public String getNs2PkgMapping(){
		String returnList="";
		TableItem[] items = namespace2packageTable.getItems();
		String packageValue; 
		for (int i=0;i<items.length;i++){
			packageValue = items[i].getText(1);
			if (packageValue!=null && !"".equals(packageValue)){
				returnList = returnList +
				             ("".equals(returnList)?"":",") +
				             items[i].getText(0)+ "=" + packageValue;
			}
			
		}
		return "".equals(returnList)?null:returnList;
	}

	public boolean isGenProxy()	{
		//boolean enabled = genProxyCheckbox_.getSelection();
		return true; //always

	}

	public void setGenerateProxy( Boolean genProxy ){
		genProxyCheckbox_.setSelection( genProxy.booleanValue() );
	}   
	
	/**
	 * Fill the combo with proper databinding names
	 */
	private void fillDatabinderCombo() {
		databindingTypeCombo.add(WSASConsumptionUIMessages.DATA_BINDING_ADB);
		databindingTypeCombo.add(WSASConsumptionUIMessages.DATA_BINDING_NONE);
		databindingTypeCombo.select(0);
	}
	
	private void populateModel() {
		model.setServiceName(serviceNameCombo.getText());
		model.setPortName(portNameCombo.getText());
		model.setPackageText(packageText.getText());
		model.setDatabindingType(databindingTypeCombo.getText());
		model.setASync(asyncOnlyRadioButton.getSelection());
		model.setSync(syncOnlyRadioButton.getSelection());
		if (syncAndAsyncRadioButton.getSelection()){
			model.setASync(true);
			model.setSync(true);
		}
		model.setGenerateAllCheck(generateAllCheckBoxButton.getSelection());
		model.setTestCaseCheck(testCaseCheckBoxButton.getSelection());
		model.setNamespaseToPackageMapping(getNs2PkgMapping());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
	 */
	public IStatus getStatus() 
	{
		IStatus result = null;
		return result;
	}

	private void populatePackageName() {
		this.packageText.setText(reader.packageFromTargetNamespace());
	}

	/**
	 * populate the service and the port from the WSDL this needs to be public
	 * since the WSDLselection page may call this
	 */
	public void populateParamsFromWSDL() {
		if (reader == null)
			reader = new WSDLPropertyReader();
		try {
			String lname = model.getWsdlURI();
			if (!"".equals(lname.trim())) {

				reader.readWSDL(lname);

				this.serviceQNameList = reader.getServiceList();
				if (!serviceQNameList.isEmpty()) {
					serviceNameCombo.removeAll();
					for (int i = 0; i < serviceQNameList.size(); i++) {
						// add the local part of the
						serviceNameCombo.add(((QName) serviceQNameList.get(i))
								.getLocalPart());
					}
					;
					// select the first one as the default
					serviceNameCombo.select(0);

					// load the ports
					loadPortNames();

				} else {
					// service name list being empty means we are switching to
					// the interface mode
					if (serviceNameCombo!=null) serviceNameCombo.removeAll();
					if (portNameCombo!=null) portNameCombo.removeAll();

				}

				populatePackageName();

				//populate the namespacess
				loadNamespaces(reader.getDefinitionNamespaceMap());
			}
		} catch (WSDLException e) {
			status = StatusUtils.errorStatus(NLS.bind(
					WSASConsumptionUIMessages.ERROR_INVALID_WSDL_FILE_READ_WRITEL,
					new String[]{e.getLocalizedMessage()}), e);
		}
	}
	
	/**
	 * Loads the namespaces
	 * @param namespaceMap
	 */
	private void loadNamespaces(Map namespaceMap){
		Iterator namespaces = namespaceMap.values().iterator();
		namespace2packageTable.removeAll();
        TableItem[] items = new TableItem[namespaceMap.size()]; // An item for each field
        
        int i = 0;
        while(namespaces.hasNext()){
        	
           items[i] = new TableItem(namespace2packageTable, SWT.NULL);
           String namespace = (String)namespaces.next();
           items[i].setText(0,namespace);
           items[i].setText(1,getPackageFromNamespace(namespace));
           i++;
        }

        namespace2packageTable.setVisible(true);
		
	}

	private void loadPortNames() {
		int selectionIndex = serviceNameCombo.getSelectionIndex();
		if (selectionIndex != -1) {
			java.util.List ports = reader.getPortNameList((QName) serviceQNameList
					.get(selectionIndex));
			if (!ports.isEmpty()) {
				portNameCombo.removeAll();
				for (int i = 0; i < ports.size(); i++) {
					// add the local part of the
					portNameCombo.add(ports.get(i).toString());
				}
				portNameCombo.select(0);
			} else {
				// error no ports found
			}
		}
	}
	
	/**
	 * get the package derived by  Namespace
	 */
	public String getPackageFromNamespace(String namespace){
		return  URLProcessor.makePackageName(namespace);
	}

}
