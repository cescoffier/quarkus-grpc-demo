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
    public Uni<Fight> triggerFight() {
        return fightService.fight(Supes.Empty.newBuilder().build())
                .map(Fight::new);
    }

    public static class Fight {
        public final Fighter hero;
        public final Fighter villain;
        public final String winner;

        public Fight(FightServiceOuterClass.Fight fight) {
            this.hero = new Fighter(fight.getHero().getName(), fight.getHero().getImage());
            this.villain = new Fighter(fight.getVillain().getName(), fight.getVillain().getImage());
            this.winner = fight.getWinner();
        }
    }

    public static class Fighter {
        public final String name;
        public final String image;

        public Fighter(String name, String image) {
            this.name = name;
            this.image = image;
        }
    }
}
