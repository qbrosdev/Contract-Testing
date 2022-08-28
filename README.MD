# Contract Testing

In contract testing, we try to test the interactions between (Micro)Services and find all the incompatibilities 
in the unit tests 😜 before publishing and discovering them in the staging area (integration tests) 😬 or even in production 💔 😲.

## Approaches
As it is described in [this post](https://stackoverflow.com/a/70000325/3593084), there are two approaches to this problem.

- **CDC** Consumer Driven Contracts
- **PDC** Producer Driven Contracts (This is a made-up name could not find any references)

### CDC 🔼
This approach is used in the Pact framework. In the Pact, each API client (consumer) defines its expectations and shares it will the API provider (producer), and the provider has to satisfy (change) those expectations. Producer API is tested against consumer expectations.

### PDC 🔽
This approach is used in Spring Cloud Contract (SCC). In SCC the API provider (Producer) defines the expectations and tests them against its implementation and then, publishes them to the API client so they can test their own behavior against the provided API 