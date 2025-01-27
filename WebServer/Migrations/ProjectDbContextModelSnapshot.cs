﻿// <auto-generated />
using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using WebServer.Models;

#nullable disable

namespace WebServer.Migrations
{
    [DbContext(typeof(ProjectDbContext))]
    partial class ProjectDbContextModelSnapshot : ModelSnapshot
    {
        protected override void BuildModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "6.0.12")
                .HasAnnotation("Relational:MaxIdentifierLength", 64);

            modelBuilder.Entity("AutomationDevice", b =>
                {
                    b.Property<int>("automationDevicesId")
                        .HasColumnType("int");

                    b.Property<int>("deviceAutomationsId")
                        .HasColumnType("int");

                    b.HasKey("automationDevicesId", "deviceAutomationsId");

                    b.HasIndex("deviceAutomationsId");

                    b.ToTable("AutomationDevice");
                });

            modelBuilder.Entity("AutomationRoom", b =>
                {
                    b.Property<int>("automationRoomsId")
                        .HasColumnType("int");

                    b.Property<int>("roomAutomationsId")
                        .HasColumnType("int");

                    b.HasKey("automationRoomsId", "roomAutomationsId");

                    b.HasIndex("roomAutomationsId");

                    b.ToTable("AutomationRoom");
                });

            modelBuilder.Entity("AutomationSensor", b =>
                {
                    b.Property<int>("SensorAutomationsId")
                        .HasColumnType("int");

                    b.Property<int>("automationSensorsId")
                        .HasColumnType("int");

                    b.HasKey("SensorAutomationsId", "automationSensorsId");

                    b.HasIndex("automationSensorsId");

                    b.ToTable("AutomationSensor");
                });

            modelBuilder.Entity("WebServer.Models.Automation", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    b.Property<int?>("active")
                        .HasColumnType("int");

                    b.Property<string>("automationTimeRange")
                        .HasColumnType("longtext");

                    b.Property<string>("automationTitle")
                        .HasColumnType("longtext");

                    b.Property<int?>("energySourceDeviceId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("energySourceDeviceId");

                    b.ToTable("Automation");
                });

            modelBuilder.Entity("WebServer.Models.Device", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    b.Property<string>("communicationProvider")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<string>("deviceName")
                        .HasColumnType("longtext");

                    b.Property<int>("roomId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("roomId");

                    b.ToTable("Device");
                });

            modelBuilder.Entity("WebServer.Models.EnergySourceDevice", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    b.Property<string>("specifications")
                        .HasColumnType("longtext");

                    b.Property<string>("title")
                        .HasColumnType("longtext");

                    b.HasKey("Id");

                    b.ToTable("EnergySourceDevice");
                });

            modelBuilder.Entity("WebServer.Models.Parameter", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    b.Property<int>("automationId")
                        .HasColumnType("int");

                    b.Property<int>("deviceId")
                        .HasColumnType("int");

                    b.Property<string>("parameterLogic")
                        .HasColumnType("longtext");

                    b.Property<string>("parameterTitle")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<string>("parameterValue")
                        .HasColumnType("longtext");

                    b.Property<int>("sensorId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("automationId");

                    b.HasIndex("deviceId");

                    b.HasIndex("sensorId");

                    b.ToTable("Parameter");
                });

            modelBuilder.Entity("WebServer.Models.Room", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    b.Property<string>("roomTitle")
                        .HasColumnType("longtext");

                    b.HasKey("Id");

                    b.ToTable("Room");
                });

            modelBuilder.Entity("WebServer.Models.Sensor", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    b.Property<string>("devId")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<int>("roomId")
                        .HasColumnType("int");

                    b.Property<string>("sensorName")
                        .HasColumnType("longtext");

                    b.HasKey("Id");

                    b.HasIndex("roomId");

                    b.ToTable("Sensor");
                });

            modelBuilder.Entity("WebServer.Models.sensorsdata", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    b.Property<string>("cluster")
                        .HasColumnType("longtext");

                    b.Property<string>("date")
                        .HasColumnType("longtext");

                    b.Property<string>("sensor")
                        .HasColumnType("longtext");

                    b.Property<string>("time")
                        .HasColumnType("longtext");

                    b.Property<string>("value")
                        .HasColumnType("longtext");

                    b.HasKey("Id");

                    b.ToTable("sensorsdata");
                });

            modelBuilder.Entity("AutomationDevice", b =>
                {
                    b.HasOne("WebServer.Models.Device", null)
                        .WithMany()
                        .HasForeignKey("automationDevicesId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("WebServer.Models.Automation", null)
                        .WithMany()
                        .HasForeignKey("deviceAutomationsId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("AutomationRoom", b =>
                {
                    b.HasOne("WebServer.Models.Room", null)
                        .WithMany()
                        .HasForeignKey("automationRoomsId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("WebServer.Models.Automation", null)
                        .WithMany()
                        .HasForeignKey("roomAutomationsId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("AutomationSensor", b =>
                {
                    b.HasOne("WebServer.Models.Automation", null)
                        .WithMany()
                        .HasForeignKey("SensorAutomationsId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("WebServer.Models.Sensor", null)
                        .WithMany()
                        .HasForeignKey("automationSensorsId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("WebServer.Models.Automation", b =>
                {
                    b.HasOne("WebServer.Models.EnergySourceDevice", "energySourceDevice")
                        .WithMany("energySourceAutomations")
                        .HasForeignKey("energySourceDeviceId");

                    b.Navigation("energySourceDevice");
                });

            modelBuilder.Entity("WebServer.Models.Device", b =>
                {
                    b.HasOne("WebServer.Models.Room", "room")
                        .WithMany("roomDevices")
                        .HasForeignKey("roomId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("room");
                });

            modelBuilder.Entity("WebServer.Models.Parameter", b =>
                {
                    b.HasOne("WebServer.Models.Automation", "automation")
                        .WithMany("AutomationParameters")
                        .HasForeignKey("automationId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("WebServer.Models.Device", "device")
                        .WithMany("deviceParameters")
                        .HasForeignKey("deviceId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("WebServer.Models.Sensor", "sensor")
                        .WithMany("SensorParameters")
                        .HasForeignKey("sensorId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("automation");

                    b.Navigation("device");

                    b.Navigation("sensor");
                });

            modelBuilder.Entity("WebServer.Models.Sensor", b =>
                {
                    b.HasOne("WebServer.Models.Room", "room")
                        .WithMany("roomSensors")
                        .HasForeignKey("roomId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("room");
                });

            modelBuilder.Entity("WebServer.Models.Automation", b =>
                {
                    b.Navigation("AutomationParameters");
                });

            modelBuilder.Entity("WebServer.Models.Device", b =>
                {
                    b.Navigation("deviceParameters");
                });

            modelBuilder.Entity("WebServer.Models.EnergySourceDevice", b =>
                {
                    b.Navigation("energySourceAutomations");
                });

            modelBuilder.Entity("WebServer.Models.Room", b =>
                {
                    b.Navigation("roomDevices");

                    b.Navigation("roomSensors");
                });

            modelBuilder.Entity("WebServer.Models.Sensor", b =>
                {
                    b.Navigation("SensorParameters");
                });
#pragma warning restore 612, 618
        }
    }
}
