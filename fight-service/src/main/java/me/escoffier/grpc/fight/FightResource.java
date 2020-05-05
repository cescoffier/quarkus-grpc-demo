package me.escoffier.grpc.fight;

import io.quarkus.grpc.runtime.annotations.GrpcService;
import io.smallrye.mutiny.Uni;
import me.escoffier.grpc.supes.FightServiceOuterClass;
import me.escoffier.grpc.supes.MutinyFightServiceGrpc;
import me.escoffier.grpc.supes.Supes;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/fight")
public class FightResource {

    @Inject @GrpcService("fight-service") MutinyFightServiceGrpc.MutinyFightServiceStub fightService;

    @GET
    @Produces("application/json")
    public Uni<FightServiceOuterClass.Fight> triggerFight() {
        return fightService.fight(Supes.Empty.newBuilder().build());

    }
}
