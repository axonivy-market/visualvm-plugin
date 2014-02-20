/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ch.ivyteam.ivy.visualvm.chart.provider;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author rwei
 */
interface IObjectType {
  String getName();

  List<IObjectInstance> getInstances() throws IOException;

}
