# BSc Data Structures - Fibonacci Heap

A historical archive of my CS BSc coursework.

## Contents

This repository contains a coauthored Java implementation of a Fibonacci heap over non-negative integer keys.

Implemented operations include:

- Insert, find-min, delete-min, delete, meld, and decrease-key.
- Counters representation by tree degree.
- Static total link and cut counters.
- Potential calculation as number of trees plus twice the number of marked nodes.

## Provenance

- Authors: Kobie Hazon and Itzchak Harel.
- Era: CS BSc.
- Last recovered work: February 2019 archive copy of a data structures practical assignment.
- Original handout status: the exact matching handout was not conclusively recovered in the canonical source folder.

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
