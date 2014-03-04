/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.data.license;

import ch.ivyteam.ivy.visualvm.chart.data.GaugeDataSource;
import ch.ivyteam.ivy.visualvm.model.IvyJmxConstant;
import ch.ivyteam.ivy.visualvm.view.IDataBeanProvider;
import eu.hansolo.steelseries.tools.Section;
import java.awt.Color;

public class NamedUsersGaugeDataSource extends GaugeDataSource {

  public NamedUsersGaugeDataSource(IDataBeanProvider dataBeanProvider, int namedUsersLimit) {
    super(dataBeanProvider, IvyJmxConstant.IvyServer.SecurityManager.NAME,
            IvyJmxConstant.IvyServer.SecurityManager.KEY_LICENSED_USERS);
    Section greenSection = new Section(0, Math.round(namedUsersLimit * 0.9), Color.GREEN);
    Section orangeSection = new Section(Math.round(namedUsersLimit * 0.9), Math.round(namedUsersLimit * 0.95),
            Color.ORANGE);
    Section redSection = new Section(Math.round(namedUsersLimit * 0.95), namedUsersLimit, Color.RED);
    getSections().add(greenSection);
    getSections().add(orangeSection);
    getSections().add(redSection);
  }

}
