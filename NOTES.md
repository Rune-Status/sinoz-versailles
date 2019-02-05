## February 6th 2019: 00:53:03

-   It is unfortunate that the ZIO-wrapped Scalaz NIO isn't released
    yet. Until a release is made, it is wise to stick to using Netty
    and manually wrap the side effects with ZIO's IO monad.

## February 3rd 2019: 21:29:18

-   The system is split up in two separate layers:
        
        - Application
        - Domain
        
    The concepts of these two layers come from Domain Driven Design where
    the a system is modeled and implemented after a (business) domain.
    As the system is modeled after the domain model, it allows software
    development teams to work closely together with domain experts and
    build and maintain a consistent design of the system that directly
    represents the capabilities of the business.
    
    The usual architectural design of a DDD-app consists of not two layers,
    but four:
        - Presentation
        - Application
        - Domain
        - Infrastructure
        
    However, DDD puts more emphasis on the domain aspect and whether the
    system is separated across these four layers is up to the developer.
    For this reason, it was decided to stick to two layers until the
    system becomes too complex and is in need of more layers.

-   Variable / Attribute descriptors and associated bit mask manipulation logic
    belongs in the application layer. The domain merely contains interfacing for
    this kind of implementation. The fact that bit mask manipulation is used
    is a technical detail and doesn't belong within the domain.
    
-   The XTEA key sets for every game map are irrelevant for the domain. This
    kind of data belongs in the application layer. These XTEA key sets are used
    by the client to gain access to encrypted map files. They should be
    represented in `MapAccessDescriptor`'s.
    
-   Translate the domain event `RoamableMapRecentered` to an application event
    so that the associated map access keys described within the
    `MapAccessDescriptor`'s can be included in said application event. This is
    necessary for the client to gain access to the map files within the
    RuneScape game cache.
    
-   The fact that varying interface positions, skill names and other constants
    come from a file labelled as 'enums' in the game cache, is an
    implementation detail. These enums should therefore be translated to 
    concise and concrete separate descriptors that are relevant to the
    domain.