package com.quack.quack_app.Infra.Adapters.Output.Persistence.Cache;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quack.quack_app.Domain.Games.Game;
import com.quack.quack_app.Domain.Reviews.Review;
import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.Users.Moderator;
import com.quack.quack_app.Domain.Users.Role;
import com.quack.quack_app.Domain.Users.Status;
import com.quack.quack_app.Domain.Users.User;
import com.quack.quack_app.Domain.ValueObjects.*;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    @Primary
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(), true);

        SimpleModule reviewsModule = new SimpleModule();

        reviewsModule.addSerializer(Reviews.class, new StdSerializer<>(Reviews.class) {
            @Override
            public void serialize(Reviews value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeStartObject();
                gen.writeStringField("@class", "com.quack.quack_app.Domain.ValueObjects.Reviews");
                gen.writeArrayFieldStart("reviews");
                for (Review review : value.reviews()) {
                    provider.defaultSerializeValue(review, gen);
                }
                gen.writeEndArray();
                gen.writeEndObject();
            }
        });

        reviewsModule.addDeserializer(Reviews.class, new StdDeserializer<>(Reviews.class) {
            @Override
            public Reviews deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
                JsonNode node = p.getCodec().readTree(p);
                JsonNode reviewsNode = node.get("reviews");
                List<Review> list = new ArrayList<>();
                if (reviewsNode != null && reviewsNode.isArray()) {
                    for (JsonNode r : reviewsNode) {
                        list.add(p.getCodec().treeToValue(r, Review.class));
                    }
                }
                return new Reviews(list);
            }
        });

        objectMapper.registerModule(reviewsModule);

        objectMapper.addMixIn(BaseUser.class, BaseUserMixin.class);
        objectMapper.addMixIn(Moderator.class, ModeratorMixin.class);
        objectMapper.addMixIn(User.class, UserMixin.class);
        objectMapper.addMixIn(Game.class, GameMixin.class);
        objectMapper.addMixIn(Review.class, ReviewMixin.class);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        ReviewRedisSerializer reviewsSerializer = new ReviewRedisSerializer(objectMapper);

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(2))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(serializer));

        RedisCacheConfiguration reviewsConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(2))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(reviewsSerializer));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration("user_reviews", reviewsConfig)
                .build();
    }

    abstract static class BaseUserMixin {
        @JsonCreator
        BaseUserMixin(
                @JsonProperty("id") UUID id,
                @JsonProperty("password") Password password,
                @JsonProperty("email") Email email,
                @JsonProperty("role") Role role,
                @JsonProperty("status") Status status,
                @JsonProperty("passwordUpdater") TokenUpdater passwordUpdater,
                @JsonProperty("emailUpdater") TokenUpdater emailUpdater,
                @JsonProperty("twoFA") TwoFA twoFA) {}
    }

    abstract static class ModeratorMixin {
        @JsonCreator
        ModeratorMixin(
                @JsonProperty("id") UUID id,
                @JsonProperty("password") Password password,
                @JsonProperty("email") Email email,
                @JsonProperty("role") Role role,
                @JsonProperty("status") Status status,
                @JsonProperty("passwordUpdater") TokenUpdater passwordUpdater,
                @JsonProperty("emailUpdater") TokenUpdater emailUpdater,
                @JsonProperty("twoFA") TwoFA twoFA) {}
    }

    abstract static class UserMixin {
        @JsonCreator
        UserMixin(
                @JsonProperty("id") UUID id,
                @JsonProperty("password") Password password,
                @JsonProperty("email") Email email,
                @JsonProperty("role") Role role,
                @JsonProperty("status") Status status,
                @JsonProperty("passwordUpdater") TokenUpdater passwordUpdater,
                @JsonProperty("emailUpdater") TokenUpdater emailUpdater,
                @JsonProperty("twoFA") TwoFA twoFA,
                @JsonProperty("username") Username username,
                @JsonProperty("photoUrl") String photoUrl,
                @JsonProperty("description") Description description,
                @JsonProperty("favoriteGames") FavoriteGames favoriteGames,
                @JsonProperty("friends") Connections friends,
                @JsonProperty("followers") Connections followers) {}
    }

    abstract static class GameMixin {
        @JsonCreator
        GameMixin(
                @JsonProperty("id") UUID id,
                @JsonProperty("name") String name,
                @JsonProperty("description") String description,
                @JsonProperty("releaseDate") LocalDate releaseDate,
                @JsonProperty("genre") String genre,
                @JsonProperty("developer") String developer,
                @JsonProperty("publisher") String publisher,
                @JsonProperty("photoUrl") String photoUrl,
                @JsonProperty("platforms") String platforms,
                @JsonProperty("rating") Rating rating,
                @JsonProperty("reviews") Reviews reviews
        ) {}
        @JsonIgnore
        abstract Reviews getNormalReviews();
    }

    abstract static class ReviewMixin {
        @JsonCreator
        ReviewMixin(
                @JsonProperty("userId") UUID userId,
                @JsonProperty("gameId") UUID gameId,
                @JsonProperty("reviewId") UUID reviewId,
                @JsonProperty("reviewDate") LocalDate reviewDate,
                @JsonProperty("status") com.quack.quack_app.Domain.Reviews.Status status,
                @JsonProperty("rating") Rating rating,
                @JsonProperty("review") String review) {}
    }
}