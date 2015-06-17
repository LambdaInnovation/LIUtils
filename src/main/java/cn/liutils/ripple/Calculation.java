package cn.liutils.ripple;

import cn.liutils.ripple.RippleException.RippleRuntimeException;

/**
 * Implementation of basic calculations
 * @author acaly
 *
 */
public final class Calculation {
    
    //IMPORTANT function names in this class is used as hard-coded string in CodeGenerator.
    
    private Calculation() {}
    
    public static Object castObject(int value) {
        return value;
    }
    
    public static Object castObject(double value) {
        return value;
    }
    
    public static Object castObject(boolean value) {
        return value;
    }
    
    public static int castInt(Object value) {
        if (value instanceof Integer)
            return (Integer) value;
        if (value instanceof Double)
        	return (int) (double) value;
        throw new RippleRuntimeException("Cannot cast to integer");
    }
    
    public static double castDouble(Object value) {
        if (value instanceof Double)
            return (Double) value;
        if(value instanceof Integer)
        	return (Integer) value;
        throw new RippleRuntimeException("Cannot cast to double");
    }
    
    public static boolean castBoolean(Object value) {
        if (value instanceof Boolean)
            return (Boolean) value;
        throw new RippleRuntimeException("Cannot cast to boolean");
    }
    
    public static boolean checkType(Object value) {
        if (value == null) return false;
        return value instanceof Integer || 
                value instanceof Double || value instanceof Boolean;
    }
    
    public static IFunction getFunctionOverload(ScriptProgram program, String path, int nargs) {
        ScriptFunction sf = program.root.getFunction(new Path(path));
        if (sf != null) {
            IFunction f = sf.cacheOverload(nargs);
            if (f != null) {
                return f;
            }
        }
        throw new RippleRuntimeException("Function '" + path + "' not found");
    }
    
    public static void throwRuntimeException(String message) {
        throw new RippleRuntimeException(message);
    }
    
    public static Object binAdd(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a + (Integer) b;
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() + ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a + (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a + ((Integer) b).doubleValue();
            }
        }
        throw new RippleRuntimeException("Unsupported operand type in binary add");
    }
    
    public static Object binSubstract(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a - (Integer) b;
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() - ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a - (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a - ((Integer) b).doubleValue();
            }
        }
        throw new RippleRuntimeException("Unsupported operand type in binary substract");
    }
    
    public static Object binMultiply(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a * (Integer) b;
            } else if (b instanceof Double) {
                return ((Integer) a).doubleValue() * ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a * (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a * ((Integer) b).doubleValue();
            }
        }
        throw new RippleRuntimeException("Unsupported operand type in binary multiply " + a.getClass() + " " + b.getClass());
    }
    
    public static Object binDivide(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return ((Integer) a).doubleValue() / (Integer) b; //always perform double divide
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() / ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a / (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a / ((Integer) b).doubleValue();
            }
        }
        throw new RippleRuntimeException("Unsupported operand type in binary divide");
    }

    public static Object binEqual(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a == (Integer) b;
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() == ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a == (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a == ((Integer) b).doubleValue();
            }
        }
        throw new RippleRuntimeException("Unsupported operand type in binary equal");
    }

    public static Object binNotEqual(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a != (Integer) b;
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() != ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a != (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a != ((Integer) b).doubleValue();
            }
        }
        throw new RippleRuntimeException("Unsupported operand type in binary not equal");
    }

    public static Object binGreater(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a > (Integer) b;
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() > ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a > (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a > ((Integer) b).doubleValue();
            }
        }
        throw new RippleRuntimeException("Unsupported operand type in binary greater");
    }

    public static Object binLesser(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a < (Integer) b;
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() < ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a < (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a < ((Integer) b).doubleValue();
            }
        }
        throw new RippleRuntimeException("Unsupported operand type in binary lesser");
    }

    public static Object binGreaterEqual(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a >= (Integer) b;
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() >= ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a >= (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a >= ((Integer) b).doubleValue();
            }
        }
        throw new RippleRuntimeException("Unsupported operand type in binary greater equal");
    }

    public static Object binLesserEqual(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a <= (Integer) b;
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() <= ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a <= (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a <= ((Integer) b).doubleValue();
            }
        }
        throw new RippleRuntimeException("Unsupported operand type in binary lesser equal");
    }

    public static Object binAnd(Object a, Object b) {
        if (a instanceof Boolean && b instanceof Boolean) {
            return (Boolean) a && (Boolean) b;
        }
        throw new RippleRuntimeException("Unsupported operand type in binary and");
    }

    public static Object binOr(Object a, Object b) {
        if (a instanceof Boolean && b instanceof Boolean) {
            return (Boolean) a || (Boolean) b;
        }
        throw new RippleRuntimeException("Unsupported operand type in binary or");
    }
    
    public static Object unMinus(Object a) {
        if (a instanceof Integer) {
            return -(Integer) a;
        } else if (a instanceof Double) {
            return -(Double) a;
        }
        throw new RippleRuntimeException("Unsupported operand type in unary minus");
    }
    
    public static Object unNot(Object a) {
        if (a instanceof Boolean) {
            return !(Boolean) a;
        }
        throw new RippleRuntimeException("Unsupported operand type in unary not");
    }
}
