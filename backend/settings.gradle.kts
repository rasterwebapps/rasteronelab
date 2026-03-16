rootProject.name = "rasteronelab-lis"

include(
    ":lis-core",
    ":lis-patient",
    ":lis-sample",
    ":lis-order",
    ":lis-result",
    ":lis-report",
    ":lis-billing",
    ":lis-inventory",
    ":lis-instrument",
    ":lis-qc",
    ":lis-admin",
    ":lis-notification",
    ":lis-integration",
    ":lis-gateway",
    ":lis-auth"
)

// Module directories
project(":lis-core").projectDir = file("lis-core")
project(":lis-patient").projectDir = file("lis-patient")
project(":lis-sample").projectDir = file("lis-sample")
project(":lis-order").projectDir = file("lis-order")
project(":lis-result").projectDir = file("lis-result")
project(":lis-report").projectDir = file("lis-report")
project(":lis-billing").projectDir = file("lis-billing")
project(":lis-inventory").projectDir = file("lis-inventory")
project(":lis-instrument").projectDir = file("lis-instrument")
project(":lis-qc").projectDir = file("lis-qc")
project(":lis-admin").projectDir = file("lis-admin")
project(":lis-notification").projectDir = file("lis-notification")
project(":lis-integration").projectDir = file("lis-integration")
project(":lis-gateway").projectDir = file("lis-gateway")
project(":lis-auth").projectDir = file("lis-auth")
