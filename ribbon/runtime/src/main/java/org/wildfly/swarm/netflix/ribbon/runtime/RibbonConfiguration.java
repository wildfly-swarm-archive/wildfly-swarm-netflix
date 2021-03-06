/**
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
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
package org.wildfly.swarm.netflix.ribbon.runtime;

import org.jboss.shrinkwrap.api.Archive;
import org.wildfly.swarm.netflix.ribbon.RibbonArchive;
import org.wildfly.swarm.netflix.ribbon.RibbonFraction;
import org.wildfly.swarm.spi.api.JARArchive;
import org.wildfly.swarm.spi.runtime.AbstractServerConfiguration;

/**
 * @author Bob McWhirter
 * @author Ken Finnigan
 */
public class RibbonConfiguration extends AbstractServerConfiguration<RibbonFraction> {

    public RibbonConfiguration() {
        super(RibbonFraction.class);
        System.setProperty("ribbon.NIWSServerListClassName", "org.wildfly.swarm.netflix.ribbon.runtime.TopologyServerList");
        System.setProperty("ribbon.NFLoadBalancerRuleClassName", "com.netflix.loadbalancer.RoundRobinRule");
    }

    @Override
    public void prepareArchive(Archive<?> archive) {
        archive.as(JARArchive.class).addModule("com.netflix.ribbon");
        archive.as(JARArchive.class).addModule("com.netflix.hystrix");
        archive.as(JARArchive.class).addModule("io.reactivex.rxjava");
        archive.as(JARArchive.class).addModule("io.reactivex.rxnetty");
        archive.as(JARArchive.class).addModule("io.netty");

        // If there hasn't been any services advertised, then advertise a service under the archive name
        if (!archive.as(RibbonArchive.class).hasAdvertised()) {
            archive.as(RibbonArchive.class).advertise();
        }
    }
}
