/**
 * <p><b>'Controller' level of the application's MVC architecture.</b> Contains classes the roles of which are :</p>
 * <p>- To make the link between the {@link fr.sos.witchhunt.model 'model' level} and the {@link fr.sos.witchhunt.view 'view' level} in terms of display, 
 * while making sure to <b>keep these two levels independent</b> of each other.</p>
 * <p>- To <b>gather user-input</b> through the {@link fr.sos.witchhunt.view 'view'} and transfer it to the {@link fr.sos.witchhunt.model 'model'}.</p>
 * <p>- To <b>manage and synchronize concurrence</b> between the different {@link fr.sos.witchhunt.view views} in use.
 * <p>- To initialize the core components and control the execution flow
 * 
 * @see fr.sos.witchhunt.controller.DisplayMediator DisplayMediator
 * 
 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
 * @see fr.sos.witchhunt.controller.interactions
 * 
 * @see fr.sos.witchhunt.controller.core
 */
package fr.sos.witchhunt.controller;