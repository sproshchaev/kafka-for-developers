rootProject.name = "kafka-for-developers"

include("webinar-01")
include("webinar-01:producer-service")
include("webinar-01:consumer-service")
findProject(":webinar-01:producer-service")?.name = "producer-service"
findProject(":webinar-01:consumer-service")?.name = "consumer-service"

include("webinar-02")
include("webinar-02:producer-service")
include("webinar-02:consumer-service")
findProject(":webinar-02:producer-service")?.name = "producer-service"
findProject(":webinar-02:consumer-service")?.name = "consumer-service"
include("webinar-03")
include("webinar-04")
include("webinar-03:consumer-service")
findProject(":webinar-03:consumer-service")?.name = "consumer-service"
include("webinar-03:producer-service")
findProject(":webinar-03:producer-service")?.name = "producer-service"
include("webinar-04:admin-service")
findProject(":webinar-04:admin-service")?.name = "admin-service"
