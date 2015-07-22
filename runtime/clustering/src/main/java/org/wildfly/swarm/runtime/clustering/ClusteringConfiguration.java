package org.wildfly.swarm.runtime.clustering;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.dmr.ModelNode;
import org.wildfly.swarm.clustering.ClusteringFraction;
import org.wildfly.swarm.runtime.container.AbstractServerConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.*;

/**
 * @author Bob McWhirter
 */
public class ClusteringConfiguration extends AbstractServerConfiguration<ClusteringFraction> {

    public ClusteringConfiguration() {
        super(ClusteringFraction.class);
    }

    @Override
    public ClusteringFraction defaultFraction() {
        return new ClusteringFraction();
    }

    @Override
    public List<ModelNode> getList(ClusteringFraction fraction) {
        List<ModelNode> list = new ArrayList<>();

        ModelNode node = new ModelNode();
        node.get(OP_ADDR).set(EXTENSION, "org.jboss.as.clustering.jgroups");
        node.get(OP).set(ADD);
        list.add(node);

        PathAddress address = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, "jgroups"));

        node = new ModelNode();
        node.get(OP_ADDR).set(address.toModelNode());
        node.get(OP).set(ADD);
        node.get("default-channel").set("ee");
        node.get("default-stack").set("udp");
        list.add(node);


        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("channel", "ee").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append( "transport", "UDP" ).toModelNode());
        node.get(OP).set(ADD);
        node.get( "socket-binding" ).set( "jgroups-udp" );
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "PING").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "MERGE3").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "FD_SOCK").toModelNode());
        node.get(OP).set(ADD);
        node.get( "socket-binding" ).set( "jgroups-udp-fd" );
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "FD_ALL").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "VERIFY_SUSPECT").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "pbcast.NAKACK2").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "UNICAST3").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "pbcast.STABLE").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "pbcast.GMS").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "UFC").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "MFC").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "FRAG2").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "udp").append("protocol", "RSVP").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);



        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").toModelNode() );
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("transport", "TCP").toModelNode() );
        node.get(OP).set(ADD);
        node.get(SOCKET_BINDING).set( "jgroups-tcp" );
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append( "protocol", "MPING" ).toModelNode() );
        node.get(OP).set(ADD);
        node.get(SOCKET_BINDING).set( "jgroups-mping" );
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "MERGE3").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "FD_SOCK").toModelNode());
        node.get(OP).set(ADD);
        node.get(SOCKET_BINDING).set( "jgroups-tcp-fd" );
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "FD").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "VERIFY_SUSPECT").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "pbcast.NAKACK2").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "UNICAST3").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "pbcast.STABLE").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "pbcast.GMS").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "MFC").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "FRAG2").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);

        node = new ModelNode();
        node.get(OP_ADDR).set(address.append("stack", "tcp").append("protocol", "RSVP").toModelNode());
        node.get(OP).set(ADD);
        list.add(node);








        return list;

    }
}