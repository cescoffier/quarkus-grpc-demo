package me.escoffier.grpc.fight;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import io.quarkus.jsonb.JsonbConfigCustomizer;

import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import java.util.Map;

@Singleton
public class JsonbCustomizer implements JsonbConfigCustomizer {

    public void customize(JsonbConfig config) {
        config.withSerializers(new ProtobufMessageJsonbSerializer());
    }

    public static class ProtobufMessageJsonbSerializer implements JsonbSerializer<GeneratedMessageV3> {
        @Override
        public void serialize(GeneratedMessageV3 message, JsonGenerator jsonGenerator,
                SerializationContext serializationContext) {
            jsonGenerator.writeStartObject();
            Map<Descriptors.FieldDescriptor, Object> fields = message.getAllFields();
            fields.forEach((desc, val) -> serializationContext.serialize(desc.getName(), val, jsonGenerator));
            jsonGenerator.writeEnd();
        }
    }

}
