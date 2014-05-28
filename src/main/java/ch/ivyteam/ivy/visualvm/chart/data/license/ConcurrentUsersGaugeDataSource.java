/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.data.license;

import ch.ivyteam.ivy.visualvm.chart.data.GaugeDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.DataBeanProvider;

public class ConcurrentUsersGaugeDataSource extends GaugeDataSource {

  public ConcurrentUsersGaugeDataSource(DataBeanProvider dataBeanProvider, int serverSessionsLimit) {
    super(dataBeanProvider, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    getThresholds().add(0.0);
    getThresholds().add(Math.floor(0.9 * serverSessionsLimit));
    getThresholds().add(1.0 * serverSessionsLimit);
    getThresholds().add(Math.floor(1.5 * serverSessionsLimit));
  }

}
