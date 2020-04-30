package me.escoffier.grpc.fight;

import io.quarkus.grpc.runtime.annotations.GrpcService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import me.escoffier.grpc.supes.FightServiceOuterClass;
import me.escoffier.grpc.supes.MutinyFightServiceGrpc;
import me.escoffier.grpc.supes.MutinySupesServiceGrpc;
import me.escoffier.grpc.supes.Supes;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Random;

@Singleton
public class FightService extends MutinyFightServiceGrpc.FightServiceImplBase {

    @Inject @GrpcService("supes-service") MutinySupesServiceGrpc.MutinySupesServiceStub supes;

    @Override
    public Uni<FightServiceOuterClass.Fight> fight(Supes.Empty request) {
        Supes.Empty empty = Supes.Empty.newBuilder().build();
        return Uni.combine().all().unis(supes.getRandomHero(empty), supes.getRandomVillain(empty)).asTuple()
                .onItem().apply(fighter -> fight(fighter.getItem1(), fighter.getItem2()));
    }

    @Override
    public Multi<FightServiceOuterClass.Fight> fightStream(Supes.Empty request) {
        return Multi.createFrom().ticks().every(Duration.ofMillis(500))
                .onItem().produceUni(x -> fight(Supes.Empty.newBuilder().build())).concatenate();
    }

    private final Random random = new Random();

    private FightServiceOuterClass.Fight fight(Supes.Hero hero, Supes.Villain villain) {

        int heroAdjust = random.nextInt(20);
        int villainAdjust = random.nextInt(20);

        FightServiceOuterClass.Fight.Builder builder = FightServiceOuterClass.Fight.newBuilder()
                .setHero(hero)
                .setVillain(villain);
        if ((hero.getLevel() + heroAdjust) >= (villain.getLevel() + villainAdjust)) {
            builder.setWinner(hero.getName());
        } else {
            builder.setWinner(villain.getName());
        }
        return builder.build();
    }
}
