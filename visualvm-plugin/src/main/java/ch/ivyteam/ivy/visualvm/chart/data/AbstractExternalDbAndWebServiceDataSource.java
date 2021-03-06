/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.data;

import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;
import java.util.logging.Logger;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 *
 * @author thnghia
 */
public abstract class AbstractExternalDbAndWebServiceDataSource extends XYChartDataSource {

  private static final Logger LOGGER
          = Logger.getLogger(AbstractExternalDbAndWebServiceDataSource.class.getName());
  private String fApplication = "*";
  private String fEnvironment = "Default";
  private String fConfigName = "*";
  private ObjectName fObjectName;
  private String fNamePattern;

  public AbstractExternalDbAndWebServiceDataSource(DataBeanProvider dataBeanProvider, String chartName,
          String xAxisDescription,
          String yAxisDescription) {
    super(dataBeanProvider, chartName, xAxisDescription, yAxisDescription);
  }

  public ObjectName getObjectName() {
    return fObjectName;
  }

  public String getApplication() {
    return fApplication;
  }

  public void setApplication(String application) {
    this.fApplication = application;
  }

  public String getEnvironment() {
    return fEnvironment;
  }

  public void setEnvironment(String environment) {
    this.fEnvironment = environment;
  }

  public String getConfigName() {
    return fConfigName;
  }

  public void setConfigName(String configName) {
    this.fConfigName = configName;
  }

  public String getNamePattern() {
    return fNamePattern;
  }

  public void setNamePattern(String namePattern) {
    fNamePattern = namePattern;
  }

  private void createObjectName(String application, String environment, String configName) {
    if (fObjectName == null) {
      String name = String.format(fNamePattern,
              application, environment, configName);
      try {
        fObjectName = new ObjectName(name);
      } catch (MalformedObjectNameException ex) {
        LOGGER.warning(ex.getMessage());
      }
    }
  }

  public void init() {
    createObjectName(fApplication, fEnvironment, fConfigName);
  }

}
