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
include("webinar-03:producer-service")
include("webinar-03:consumer-service")
findProject(":webinar-03:producer-service")?.name = "producer-service"
findProject(":webinar-03:consumer-service")?.name = "consumer-service"

include("webinar-04")
include("webinar-04:admin-service")
findProject(":webinar-04:admin-service")?.name = "admin-service"

include("webinar-05")
include("webinar-05:producer-service")
include("webinar-05:consumer-service")
findProject(":webinar-05:producer-service")?.name = "producer-service"
findProject(":webinar-05:consumer-service")?.name = "consumer-service"

include("webinar-06")
include("webinar-06:producer-service")
include("webinar-06:stream-service")
findProject(":webinar-06:producer-service")?.name = "producer-service"
findProject(":webinar-06:stream-service")?.name = "stream-service"

include("webinar-07")
include("webinar-07:producer-service")
include("webinar-07:consumer-service")
findProject(":webinar-07:producer-service")?.name = "producer-service"
findProject(":webinar-07:consumer-service")?.name = "consumer-service"

include("webinar-08")

include("webinar-09")
include("webinar-09:producer-service")
include("webinar-09:consumer-service")
findProject(":webinar-09:producer-service")?.name = "producer-service"
findProject(":webinar-09:consumer-service")?.name = "consumer-service"
