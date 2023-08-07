### 아이템 11 - equals를 재정의하려거든 hashCode도 재정의하라

**equals를 재정의한 클래스 모두에서 hashCode도 재정의해야 한다.** 

그렇지 않으면 hashCode 일반 규약을 어기게 되어 Hash를 사용하는 자료구조에서 문제를 일으킨다.

- equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메서드는 일관되게 같은 값을 반환해야 한다.
- equals가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 한다.
- equals가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다.
    - **다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아진다.**

이 세가지 조건 중에 가장 문제가 될 수 있는 부분은 두 번째다. <br>
논리적으로 같은 객체는 같은 해시코드를 반환해야 한다. 

equals는 물리적으로 다른 두 객체를 논리적으로 같다고 할 수 있다. Object의 기본 hashCode 메서드는 <br>
이 둘이 전혀 다르다고 판단하여, **규약과 달리 서로 다른 값을 반환한다.**

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/783b6b1b-d792-4959-a5cc-7fc1b5adc4e0)

Object의 기본 HashCode 메서드는 객체가 가지고 있는 값 필드에 따라서 hashCode를 생성하는 것이 아닌, <br>
JVM이 각 객체마다 고유한 코드를 부여하게 되는데 그 코드를 반환하고 있다.

- **Hash 자료구조 사용시의 문제점**

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/68b0072b-5175-4470-801b-9ed89cbd8e71)

Student 인스턴스인 student1과 student2는 다른 인스턴스 이지만 논리적으로 동치다. <br>
따라서 같은 hashCode를 반환해야 한다. equals 메서드는 규약에 맞게 오버라이딩 된 상태다.

하지만 hashCode 메서드를 오버라이딩 하지 않았기 때문에 논리적 동치 임에도 전혀 다른 해시코드가 나온다.

hashCode 메서드를 오버라이딩 하지 않아서 문제가 생기는 곳은 Hash 자료구조를 사용할때 생긴다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a8aafc9b-674b-4ac9-aa26-0312adbd5061)

Map에 student1 인스턴스를 넣고 student2 인스턴스로 value를 꺼내오자 null이 출력되게 된다. <br>
HashMap의 구현을 보면 쉽게 이해할 수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/5e2c1f20-47f4-4686-87de-50669e67c7cf)

HashMap의 put 메서드에서 내부적으로 호출해 사용하는 메서드이다. <br>
key를 hash 함수를 이용해 해시값을 생성하는 역할을 하는데, key의 hashCode 반환값을 기준으로 추가적인 <br>
작업을 하는 것을 볼 수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/65e86632-6944-471b-8926-6d8ed159b871)

HashMap의 get 메서드에서도 해시 값을 기반으로 해시 테이블에서 값을 가져오기 때문에, 만들어진 해시 값을 <br>
이용해 해시 테이블에서 검색을 시도했을때 값이 없기 때문에 null이 반환되는 것이다.

- **최악의 hashCode 구현으로 인해 생기는 문제**
  
![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/c97dd19e-194a-4da8-bb74-f05166189514)

이런 구현은 절대 하지말아야 할 코드이기도 하다. <br>
모든 객체에게 똑같은 값만 내어주기 때문에 모든 객체가 해시 테이블 버킷 하나에 담겨서 마치 연결 리스트와
같이 동작하게 된다.

해시 테이블 자료구조를 사용하는 이유가 O(1) 시간 복잡도 안에 데이터를 찾을 수 있다는 장점인데, <br>
모든  객체가 같은 버킷에 담겨 체이닝 된다면 O(n)으로 시간 복잡도가 기하급수적으로 늘어난다.

종국에는 객체가 많아지면 쓰지 못할 정도로 나쁜 성능을 보여주게 된다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/cf4b21a1-e032-426c-a877-36807c81f496)

이 코드는 내부 값이 다른 객체도 같은 hashCode를 반환하는 문제가 있다.

- **좋은 해시 함수를 만드는 법**
    
좋은 해시 함수는 서로 다른 인스턴스에 다른 해시코드를 반환하는 것이다. <br>
이상적인 해시 함수라면 서로 다른 인스턴스들을 32비트 정수 범위에 균일하게 분배해야 한다.

왜 하필 32비트 정수 범위에 균일하게 분배하려고 할까? <br>
물론 더 큰 비트 단위의 해시 함수도 존재하고 만들 수도 있다. 32비트 라는 정수 범위는 컴퓨터의 워드 <br>
단위와 같다는 점에서 장점이였지만, **요즘은 64비트가 기본 처리량인 컴퓨터가 많다.**

물론 32비트 해시 함수보다는 64비트 해시 함수가 메모리도 더 많이 점유하고 성능적으로 떨어질 수 있다. <br>
성능을 약간 포기하더라도, 강력한 보안이 필요하거나 충돌 가능성이 낮아야 하는 프로그램의 경우 <br>
**64비트 또는 그 이상의 해시 함수를 고려할 수 있다.** 

즉 이상적인 해시 함수란 만들고자 하는 애플리케이션의 용도나 규모에 따라 달라질 수 있다.

- **좋은 해시함수 작성 순서**

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/cc799b70-0d73-4b54-9a8b-de5706b05e8c)

필드가 배열인 경우는 다루지 않았지만, 배열의 핵심 원소만 별도 필드로 다루는 방법이 있고 <br>
배열에 핵심 원소가 하나도 없다면 0을, 모든 원소가 핵심 원소라면 Arrays.hashCode를 사용하는 방법이있다.

equals와 마찬가지로 파생 필드는 해시코드 계산에서 제외해도 된다. <br>
또한 equals 에서 사용하지 않는 필드는 해시코드 계산에서 사용해서는 안된다. 

hashCode를 계산할때 31 이라는 특정 숫자를 곱해주는 이유는 곱하는 순서에 따라 result 값이 달라지기 때문이다. <br>
**String의 hashCode를 곱셈없이 구현한다고 가정하면 모든 아나그램의 해시코드가 같아 진다.**

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/6afea15c-7863-44d1-bbb2-3a4c328ff64e)

곱셈이 없는 hashCode 메서드를 이용하면 같은 아나그램의 경우 해시코드가 모두 같다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/6701bdc6-618b-4b30-9a69-08678e0f02c1)

서로 다른 문자지만 아나그램이 같아, 해시 코드의 값이 같은 것을 볼 수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/62adc1da-62b8-4c23-b3f7-ed73e58eb39a)

이제 특정 숫자를 곱해서 계산하는 해시 코드 생성 메서드를 호출하면 다른 해시 값이 나오는 것을 볼  수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/8d99c0ea-6cc5-4621-9c38-8e30a8cfd964)

**곱할 숫자를 31로 정한 이유는 31이 홀수이면서 소수(prime)이기 때문이다.**
또한 31은 메르센 소수라는 소수 중에서도 특수한 성질을 가진 소수이기에 많은 이점을 가져다준다.

1. **"Avalanche Effect"를 유발하는 속성**: 메르센 소수를 해시 함수의 크기로 사용하면 해시 결과의 각 비트가 입력 데이터의 작은 변화에 민감하게 반응하게 된다. 이러한 특성은 해시 코드 간의 유사성이 작은 차이로 인해 크게 달라지는 "Avalanche Effect"를 유발하며, 데이터의 작은 변화가 해시 코드에 전체적으로 미치는 영향을 효과적으로 퍼뜨린다.
2. **충돌 감소**: 메르센 소수를 사용하면 해시 충돌 가능성을 줄일 수 있다. 메르센 소수는 큰 크기의 비트를 가지기 때문에 해시 충돌이 발생할 확률이 낮고, 데이터가 더 고르게 분산되도록 도와주며, 충돌 가능성을 최소화하는 데 도움이 된다.
3. **소수의 수학적 특성 활용**: 메르센 소수는 수학적 특성이 잘 연구되어 있는 소수로서, 소수의 특성을 활용하여 해시 함수를 설계하면 충돌을 효과적으로 관리할 수 있다.
4. **주기적 패턴 회피**: 일부 해시 함수에서는 주기적인 패턴이 해시 값의 반복을 유발하여 해시 충돌 가능성을 증가시킬 수 있는 반면에, 메르센 소수를 사용하면 주기적인 패턴을 피할 수 있어 해시 함수의 안전성을 높일 수 있다.

곱하는 숫자가 짝수이고 오버플로가 발생한다면 정보를 잃게 된다는 문제 때문에 짝수를 쓰지 말라는 이유도 있다.

결과적으로 31을 이용하면, 이 곱셈을 시프트 연산과 뺄셈으로 최적화 할 수 있다. <br>
요즘 VM들은 이런 최적화를 자동으로 해주기 때문에 성능에서도 이점을 누릴 수 있다.

Objects 클래스는 임의의 개수만큼 객체를 받아 해시코드를 계산해주는 정적 메서드인 hash를 제공하고 있다. <br>
직접 구현한 hashCode와 비슷한 수준의 hashCode 함수를 단 한줄로 작성할 수 있다는 장점이 있다.

아쉽지만 입력 인수를 담기위한 배열이 만들어지고, 입력 중 기본 타입에 대한 박싱과 언박싱이 있어 성능은 떨어진다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/bb3f09d3-aa9e-476f-9dab-94329aab8152)

Objects의 hash 메서드는 내부적으로 Arrays.hashCode를 호출해 사용한다.

- **hashCode 정의시 주의사항**
    - 해시코드를 계산할 때 핵심 필드를 생략해서는 안된다.
        - 성능은 오르겠지만 해시 품질이 나빠져 해시테이블의 성능을 심각하게 떨어뜨릴 수 있다.
    - hashCode가 반환하는 값의 생성 규칙을 API 사용자에게 자세히 공표하지 말아야 한다.
        - 클라이언트가 이 값에 의지하지 않게 되고, 추후에 게산 방식을 바꿀 수 있기 때문이다.
        - 생성 규칙 공표로 인해 이미 클라이언트가 이 값에 의지하고 있다면 향후 릴리즈에서 <br> 해시 함수의 개선을 할 여지마저 없애버린 것이다.












