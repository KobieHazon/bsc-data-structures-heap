# BSc Data Structures - Fibonacci Heap

- Authors: Kobie Hazon and Itzchak Harel.
- Course: Data Structures.

## Contents

This repository contains a coauthored Java implementation of a Fibonacci heap over non-negative integer keys.

Implemented operations include:

- Insert, find-min, delete-min, delete, meld, and decrease-key.
- Counters representation by tree degree.
- Static total link and cut counters.
- Potential calculation as number of trees plus twice the number of marked nodes.

## Tech Stack

- Java, validated with Java 11 or newer.
- Plain `javac` and `java`; no external dependencies.
- `make` for repeatable compile, test, and cleanup commands.

## Run

```bash
make test
```

To remove generated files:

```bash
make clean
```

## Assignment and report

- `assignment/fibonacci-heap-reference.pdf`: earlier course handout with the same heap API and measurement exercises.
- `report/heap-report.pdf`: my report with Itzchak Harel, including implementation details and measurement results.
