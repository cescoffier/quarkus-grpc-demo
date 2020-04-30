package me.escoffier.grpc.supes;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.smallrye.mutiny.Uni;

import java.util.Random;

@MongoEntity(collection = "villains")
public class Villain extends ReactivePanacheMongoEntity {

    public String name;
    public int level;
    public String image;

    public static Uni<Villain> findRandom() {
        return Villain.count()
                .map(count -> {
                    Random random = new Random();
                    return random.nextInt(count.intValue());
                })
                .flatMap(index -> Villain.findAll().page(index, 1).firstResult());
    }
}
