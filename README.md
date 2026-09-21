# BSc Data Structures - Fibonacci Heap

- Authors: Kobie Hazon and Itzchak Harel.
- Course: BSc Computer Science.
- Available copy: February 2019.
- Assignment brief: The exact matching handout has not been confirmed.

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
