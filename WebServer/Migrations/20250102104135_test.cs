using Microsoft.EntityFrameworkCore.Migrations;
using MySql.EntityFrameworkCore.Metadata;

#nullable disable

namespace WebServer.Migrations
{
    public partial class test : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "EnergySourceDevice",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySQL:ValueGenerationStrategy", MySQLValueGenerationStrategy.IdentityColumn),
                    title = table.Column<string>(type: "longtext", nullable: true),
                    specifications = table.Column<string>(type: "longtext", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_EnergySourceDevice", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Room",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySQL:ValueGenerationStrategy", MySQLValueGenerationStrategy.IdentityColumn),
                    roomTitle = table.Column<string>(type: "longtext", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Room", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "sensorsdata",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySQL:ValueGenerationStrategy", MySQLValueGenerationStrategy.IdentityColumn),
                    cluster = table.Column<string>(type: "longtext", nullable: true),
                    sensor = table.Column<string>(type: "longtext", nullable: true),
                    date = table.Column<string>(type: "longtext", nullable: true),
                    time = table.Column<string>(type: "longtext", nullable: true),
                    value = table.Column<string>(type: "longtext", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_sensorsdata", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Automation",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySQL:ValueGenerationStrategy", MySQLValueGenerationStrategy.IdentityColumn),
                    active = table.Column<int>(type: "int", nullable: true),
                    automationTitle = table.Column<string>(type: "longtext", nullable: true),
                    automationTimeRange = table.Column<string>(type: "longtext", nullable: true),
                    energySourceDeviceId = table.Column<int>(type: "int", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Automation", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Automation_EnergySourceDevice_energySourceDeviceId",
                        column: x => x.energySourceDeviceId,
                        principalTable: "EnergySourceDevice",
                        principalColumn: "Id");
                });

            migrationBuilder.CreateTable(
                name: "Device",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySQL:ValueGenerationStrategy", MySQLValueGenerationStrategy.IdentityColumn),
                    deviceName = table.Column<string>(type: "longtext", nullable: true),
                    roomId = table.Column<int>(type: "int", nullable: false),
                    communicationProvider = table.Column<string>(type: "longtext", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Device", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Device_Room_roomId",
                        column: x => x.roomId,
                        principalTable: "Room",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Sensor",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySQL:ValueGenerationStrategy", MySQLValueGenerationStrategy.IdentityColumn),
                    sensorName = table.Column<string>(type: "longtext", nullable: true),
                    devId = table.Column<string>(type: "longtext", nullable: false),
                    roomId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Sensor", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Sensor_Room_roomId",
                        column: x => x.roomId,
                        principalTable: "Room",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "AutomationRoom",
                columns: table => new
                {
                    automationRoomsId = table.Column<int>(type: "int", nullable: false),
                    roomAutomationsId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AutomationRoom", x => new { x.automationRoomsId, x.roomAutomationsId });
                    table.ForeignKey(
                        name: "FK_AutomationRoom_Automation_roomAutomationsId",
                        column: x => x.roomAutomationsId,
                        principalTable: "Automation",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_AutomationRoom_Room_automationRoomsId",
                        column: x => x.automationRoomsId,
                        principalTable: "Room",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "AutomationDevice",
                columns: table => new
                {
                    automationDevicesId = table.Column<int>(type: "int", nullable: false),
                    deviceAutomationsId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AutomationDevice", x => new { x.automationDevicesId, x.deviceAutomationsId });
                    table.ForeignKey(
                        name: "FK_AutomationDevice_Automation_deviceAutomationsId",
                        column: x => x.deviceAutomationsId,
                        principalTable: "Automation",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_AutomationDevice_Device_automationDevicesId",
                        column: x => x.automationDevicesId,
                        principalTable: "Device",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "AutomationSensor",
                columns: table => new
                {
                    SensorAutomationsId = table.Column<int>(type: "int", nullable: false),
                    automationSensorsId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AutomationSensor", x => new { x.SensorAutomationsId, x.automationSensorsId });
                    table.ForeignKey(
                        name: "FK_AutomationSensor_Automation_SensorAutomationsId",
                        column: x => x.SensorAutomationsId,
                        principalTable: "Automation",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_AutomationSensor_Sensor_automationSensorsId",
                        column: x => x.automationSensorsId,
                        principalTable: "Sensor",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Parameter",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySQL:ValueGenerationStrategy", MySQLValueGenerationStrategy.IdentityColumn),
                    parameterTitle = table.Column<string>(type: "longtext", nullable: false),
                    parameterLogic = table.Column<string>(type: "longtext", nullable: true),
                    parameterValue = table.Column<string>(type: "longtext", nullable: true),
                    sensorId = table.Column<int>(type: "int", nullable: false),
                    deviceId = table.Column<int>(type: "int", nullable: false),
                    automationId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Parameter", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Parameter_Automation_automationId",
                        column: x => x.automationId,
                        principalTable: "Automation",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Parameter_Device_deviceId",
                        column: x => x.deviceId,
                        principalTable: "Device",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Parameter_Sensor_sensorId",
                        column: x => x.sensorId,
                        principalTable: "Sensor",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Automation_energySourceDeviceId",
                table: "Automation",
                column: "energySourceDeviceId");

            migrationBuilder.CreateIndex(
                name: "IX_AutomationDevice_deviceAutomationsId",
                table: "AutomationDevice",
                column: "deviceAutomationsId");

            migrationBuilder.CreateIndex(
                name: "IX_AutomationRoom_roomAutomationsId",
                table: "AutomationRoom",
                column: "roomAutomationsId");

            migrationBuilder.CreateIndex(
                name: "IX_AutomationSensor_automationSensorsId",
                table: "AutomationSensor",
                column: "automationSensorsId");

            migrationBuilder.CreateIndex(
                name: "IX_Device_roomId",
                table: "Device",
                column: "roomId");

            migrationBuilder.CreateIndex(
                name: "IX_Parameter_automationId",
                table: "Parameter",
                column: "automationId");

            migrationBuilder.CreateIndex(
                name: "IX_Parameter_deviceId",
                table: "Parameter",
                column: "deviceId");

            migrationBuilder.CreateIndex(
                name: "IX_Parameter_sensorId",
                table: "Parameter",
                column: "sensorId");

            migrationBuilder.CreateIndex(
                name: "IX_Sensor_roomId",
                table: "Sensor",
                column: "roomId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "AutomationDevice");

            migrationBuilder.DropTable(
                name: "AutomationRoom");

            migrationBuilder.DropTable(
                name: "AutomationSensor");

            migrationBuilder.DropTable(
                name: "Parameter");

            migrationBuilder.DropTable(
                name: "sensorsdata");

            migrationBuilder.DropTable(
                name: "Automation");

            migrationBuilder.DropTable(
                name: "Device");

            migrationBuilder.DropTable(
                name: "Sensor");

            migrationBuilder.DropTable(
                name: "EnergySourceDevice");

            migrationBuilder.DropTable(
                name: "Room");
        }
    }
}
