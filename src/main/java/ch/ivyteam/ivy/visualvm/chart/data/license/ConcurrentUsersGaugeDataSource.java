/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.data.license;

import ch.ivyteam.ivy.visualvm.chart.data.GaugeDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import eu.hansolo.steelseries.tools.Section;
import java.awt.Color;

public class ConcurrentUsersGaugeDataSource extends GaugeDataSource {

  public ConcurrentUsersGaugeDataSource(IDataBeanProvider dataBeanProvider, int serverSessionsLimit) {
    super(dataBeanProvider, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_SESSIONS);
    Section greenSection = new Section(0, Math.floor(serverSessionsLimit * 0.9), new Color(110, 184, 37));
    Section orangeSection = new Section(Math.floor(serverSessionsLimit * 0.9), serverSessionsLimit,
            new Color(255, 210, 10));
    Section redSection = new Section(serverSessionsLimit, Math.floor(serverSessionsLimit * 1.5),
            new Color(240, 40, 40));
    getSections().add(greenSection);
    getSections().add(orangeSection);
    getSections().add(redSection);
  }

}
