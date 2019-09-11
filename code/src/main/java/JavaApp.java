import play.libs.ws.StandaloneWSResponse;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static stuff.JavaStuff.*;

class JavaApp {

    public static void main(String[] args) {
        Function<String, Integer> count = String::length;

        var result = ws.apply(client -> {
            Supplier<CompletableFuture<String>> randomWord = () -> client.url(randomWordUrl).get().thenApply(StandaloneWSResponse::getBody).toCompletableFuture();

            return client.url(randomNumUrl).get().thenCompose( response -> {
                var num = Integer.parseInt(response.getBody());
                return sequence(randomWord, num).thenApply(words -> String.join(" ", words));
            });
        });

        result.thenRun(System.out::println);
    }
}
