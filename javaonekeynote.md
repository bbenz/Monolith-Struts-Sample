

# Java at AI Scale — JavaOne 2026 Keynote Demo

This repository supports the Microsoft segment of the **JavaOne 2026 keynote**.
If you watched the session live or recorded, this repo lets you explore the same application and workflow shown on stage.

The keynote demonstrates a single continuous story:

**A real Java application is modernized, deployed, and made intelligent — end to end.**

The goal is to show how modern Java applications can be:

* Modernized incrementally
* Integrated with AI models and agents
* Deployed with platform-aware runtime optimization
* Operated with enterprise controls for security and reliability

All while staying within familiar Java tools and workflows.

---

# The Message Behind the Demo

Modern Java applications are being rebuilt faster, run more efficiently, and made intelligent without requiring developers to abandon existing tools, frameworks, or practices.

AI is not replacing enterprise Java systems.
It is helping teams modernize and extend them safely and incrementally.

This repo shows one practical path:

**Legacy → Modern → Intelligent → High-performance Java**

---

# What This Repository Contains

This repository centers on a legacy Java web application that evolves across the keynote:

* A Java 8 Struts application
* Modernized to Java 25 and Spring Boot
* Extended with AI capabilities
* Deployed to Azure Kubernetes Service
* Optimized at runtime using platform intelligence

You can explore each stage independently or follow the same path shown in the keynote.

---

# The Journey Demonstrated in the Keynote

## 1. Modernize the Code and Improve Developer Productivity

We begin with a legacy Java application:

* Java 8
* Struts-based web architecture
* Outdated dependencies and patterns

Using GitHub Copilot and the App Modernization agent, we:

* Analyze deprecated APIs and risks
* Generate modernization recommendations
* Migrate to Java 25
* Transition from Struts to Spring Boot
* Produce a modernization plan for incremental upgrades

The focus is not automated rewriting.
The focus is **understanding and guiding modernization decisions** while keeping developers in control.

Key idea:
AI acts as a thinking partner during modernization, not just a code generator.

---

## 2. Add Intelligence to the Application

After modernization, we introduce AI capabilities using Microsoft Foundry.

In the keynote, we:

* Select and deploy a model in Foundry
* Compare model capabilities and safety attributes
* Generate Java integration code
* Add an intelligent workflow to the application (Ski Trip Advisor)

The application now calls a deployed model through generated Java services and classes.

Key ideas:

* Model choice remains flexible
* Enterprise controls are handled through the platform
* Data isolation and privacy are preserved
* Java remains Java — no new mental model required

Intelligence becomes part of the application architecture rather than a separate system.

---

## 3. Modernize How the Application Runs

With modernization and intelligence in place, the application is deployed and optimized.

We deploy the containerized application to **Azure Kubernetes Service (AKS)** and introduce **Java Azure Launcher (JAZ)**.

JAZ automatically applies environment-aware optimizations:

* JVM tuning
* Memory sizing
* Startup optimization
* Container-aware configuration

Instead of manually tuning JVM flags and runtime parameters, the platform applies recommended settings based on where the application runs.

Key idea:
Modernization includes runtime intelligence and operational efficiency, not just code changes.

---

# What You Can Explore in This Repo

You can use this repository to follow the same journey demonstrated in the keynote.

## Explore the legacy application

Review the original Java 8 Struts codebase and structure.

## Review modernization artifacts

See how modernization plans and upgraded code are structured.

## Examine the intelligent application components

Look at the generated model and service classes used for the Ski Trip Advisor workflow.

## Review deployment and runtime configuration

Explore prompts and configuration used for deploying to AKS and optimizing with JAZ.

You can move through these stages at your own pace rather than as a live demo.

---

# Suggested Next Steps

If you want to try this workflow yourself:

### Modernize an existing Java application

Use GitHub Copilot and modernization agents within your IDE to analyze and upgrade legacy codebases.

### Add AI capabilities to Java applications

Use Microsoft Foundry with Spring AI or LangChain4j to integrate models into Java services and workflows.

### Deploy and optimize on Kubernetes

Deploy modern Java applications to Kubernetes and apply runtime optimization using JAZ.

---

# Why This Matters for Java Teams

Most enterprise Java systems must be:

* Secure
* Reliable
* Maintainable
* Long-lived

AI adoption in these environments requires:

* Incremental modernization
* Developer control
* Platform-level safety and governance
* Runtime efficiency at scale

The workflow demonstrated in the keynote and in this repository is designed to support those requirements.

---

# Related Resources

(Replace or expand as needed)

* GitHub Copilot for Java
* Microsoft Foundry
* Spring AI and LangChain4j
* Azure Kubernetes Service
* Java Azure Launcher (JAZ)

---

# Closing Thought

Modern Java development is evolving across the full lifecycle:

* How applications are modernized
* How intelligence is integrated
* How runtime environments are optimized

This repository provides a working example of that evolution in a single continuous application story.

