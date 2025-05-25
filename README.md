### Technical stack

- Kotlin
- UI: Jetpack Compose
- DI: Hilt
- Network: Ktor
- Image Loading: Coil
- Serialization: KotlinXSerialization
- Paging: Paging3
- [kotlinx-collections](https://github.com/Kotlin/kotlinx.collections.immutable)
- mindSdk = 25 (Android 7) & targetSdk = 36 (Android 16)

### Static Analysis Tools

- Detekt
- [Detekt-Compose-Rules](https://mrmans0n.github.io/compose-rules/)
- Detekt-Formatting

### Architecture and other Technical details

**IMPORTANT**: I would never include the API key inside the project! It is there only for testing purposes.

The app architecture follows Clean Architecture principles and Android’s official architecture guidelines. 
For the UI, I use Jetpack Compose and MVVM as the design pattern. 

The app consists of a single module and is organized into four main layers:
- `data` Contains DTO models, network clients (Ktor and PagingSource), and repository implementations
- `domain` Contains domain models, use cases, and repository interfaces
- `ui` Contains UI components
- `di` Contains dependency injection setup

The Ktor `HttpClient` is configured in the di module and includes a retry mechanism.

For Composables, `kotlinx.collections.immutable` is used to leverage immutable collections and improve Compose state handling.


### Potential Improvements if more time was available :)
- modularization (e.g. by feature or by layer)
- fine-grained error model
- more unit tests
- In production code, I **always** specify Coroutine Dispatchers. For this sample, I rely on Ktor’s default use of the IO dispatcher.
- Retry button when loading fails
